package com.coderscampus.assignment13.service;

import java.util.Optional;
import java.util.Set;
import javax.transaction.Transactional;

import com.coderscampus.assignment13.domain.Address;
import com.coderscampus.assignment13.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coderscampus.assignment13.domain.Account;
import com.coderscampus.assignment13.domain.User;
import com.coderscampus.assignment13.repository.AccountRepository;
import com.coderscampus.assignment13.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private AccountRepository accountRepo;
	@Autowired
	private AddressRepository addressRepo;


	public Set<User> findAll () {
		return userRepo.findAllUsers();
	}

	public User findById(Long userId) {
		Optional<User> userOpt = userRepo.findById(userId);
		return userOpt.orElse(new User());
	}

	public User saveUser(User user) {
		if (user.getUserId() == null) {
			Account checking = new Account();
			checking.setAccountName("Checking Account");
			checking.getUsers().add(user);

			Account savings = new Account();
			savings.setAccountName("Savings Account");
			savings.getUsers().add(user);

			accountRepo.save(checking);
			accountRepo.save(savings);

			user.getAccounts().add(checking);
			user.getAccounts().add(savings);
		}
		Address address = user.getAddress();
		if (address != null) {
			addressRepo.save(address);
		}
		return userRepo.save(user);
	}

	public void delete(Long userId) {
		userRepo.deleteById(userId);
	}

	@Transactional
	public User createNewAccount(Long userId, Account account) {
		User user = findById(userId);
		if (user != null) {
			account.setAccountId(null);
			accountRepo.save(account);
			System.out.println("Account saved: " + account.getAccountId());

			account.getUsers().add(user);
			user.getAccounts().add(account);
			userRepo.save(user);
			System.out.println("User saved: " + user.getUserId());
			return  user;
		}
		return null;
	}
}
