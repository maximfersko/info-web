package com.fersko.info.service.impl;

import com.fersko.info.dto.FriendDto;
import com.fersko.info.mapper.FriendMapper;
import com.fersko.info.repository.impl.FriendRepositoryImpl;
import com.fersko.info.service.FriendService;

import java.util.List;
import java.util.Optional;

public class FriendServiceImpl implements FriendService {

    private final FriendRepositoryImpl friendRepositoryImpl;

    private final FriendMapper friendMapper = new FriendMapper();

    public FriendServiceImpl(FriendRepositoryImpl friendRepositoryImpl) {
        this.friendRepositoryImpl = friendRepositoryImpl;
    }


    @Override
    public Optional<FriendDto> findById(Long id) {
        return friendRepositoryImpl.findById(id).map(friendMapper::toDto);
    }

    @Override
    public void update(FriendDto entity) {
        friendRepositoryImpl.update(friendMapper.toEntity(entity));
    }

    @Override
    public boolean delete(Long id) {
        return friendRepositoryImpl.delete(id);
    }

    @Override
    public FriendDto save(FriendDto entity) {
        return friendMapper.toDto(friendRepositoryImpl.save(friendMapper.toEntity(entity)));
    }

    @Override
    public List<FriendDto> findByAll() {
        return friendRepositoryImpl.findByAll()
                .stream()
                .map(friendMapper::toDto)
                .toList();
    }
}
