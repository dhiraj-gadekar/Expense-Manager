package com.expensemanager.servise;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.expensemanager.entity.User;
import com.expensemanager.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean isUsernameExists(String username) {

        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            
            return false;
        }
        return true;
    }
    
    public List<Integer> getAllUserIds() {
        
        Optional<List<Integer>> fetchAllUserIds = userRepository.fetchAllUserIds();
        if (fetchAllUserIds.isPresent()) {

            return fetchAllUserIds.get();
        }
        return new ArrayList<>();
    }
}
