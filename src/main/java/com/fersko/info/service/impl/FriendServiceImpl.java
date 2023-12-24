package com.fersko.info.service.impl;

import com.fersko.info.dto.FriendDto;
import com.fersko.info.mapper.FriendMapper;
import com.fersko.info.repository.FriendRepository;
import com.fersko.info.repository.impl.FriendRepositoryImpl;
import com.fersko.info.service.FriendService;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class FriendServiceImpl implements FriendService {
    private final FriendRepository friendRepositoryImpl;
    private final FriendMapper friendMapper;

    public FriendServiceImpl() {
        friendRepositoryImpl = new FriendRepositoryImpl();
        friendMapper = new FriendMapper();
    }


    @Override
    public Optional<FriendDto> findById(Long id) {
        return friendRepositoryImpl.findById(id).map(friendMapper::toDto);
    }

    @Override
    public FriendDto update(FriendDto entity) {
        return friendMapper.toDto(friendRepositoryImpl.update(friendMapper.fromDto(entity)));
    }

    @Override
    public boolean delete(Long id) {
        return friendRepositoryImpl.delete(id);
    }

    @Override
    public FriendDto save(FriendDto entity) {
        return friendMapper.toDto(friendRepositoryImpl.save(friendMapper.fromDto(entity)));
    }

    @Override
    public List<FriendDto> findByAll() {
        return friendRepositoryImpl.findByAll()
                .stream()
                .map(friendMapper::toDto)
                .toList();
    }
}
