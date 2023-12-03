package com.fersko.info.service;

import com.fersko.info.entity.Friends;
import com.fersko.info.repository.FriendsRepository;

import java.util.List;
import java.util.Optional;

public class FriendsService implements BaseService<Friends, Integer> {

    private final FriendsRepository friendsRepository;

    public FriendsService(FriendsRepository friendsRepository) {
        this.friendsRepository = friendsRepository;
    }

    @Override
    public Optional<Friends> findById(Integer id) {
        return friendsRepository.findById(id);
    }

    @Override
    public void update(Friends entity) {
        friendsRepository.update(entity);
    }

    @Override
    public boolean delete(Integer id) {
        return friendsRepository.delete(id);
    }

    @Override
    public Friends save(Friends entity) {
        return friendsRepository.save(entity);
    }

    @Override
    public List<Friends> findByAll() {
        return friendsRepository.findByAll();
    }
}
