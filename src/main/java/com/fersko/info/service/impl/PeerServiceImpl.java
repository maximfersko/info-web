package com.fersko.info.service.impl;

import com.fersko.info.dto.PeerDto;
import com.fersko.info.entity.Peer;
import com.fersko.info.mapper.PeerMapper;
import com.fersko.info.repository.impl.PeerRepositoryImpl;
import com.fersko.info.service.BaseService;
import com.fersko.info.service.PeerService;

import java.util.List;
import java.util.Optional;

public class PeerServiceImpl implements PeerService {

    private final PeerRepositoryImpl peerRepositoryImpl;

    private final PeerMapper peerMapper = new PeerMapper();

    public PeerServiceImpl(PeerRepositoryImpl peerRepositoryImpl) {
        this.peerRepositoryImpl = peerRepositoryImpl;
    }


    @Override
    public Optional<PeerDto> findById(String id) {
        return peerRepositoryImpl.findById(id).map(peerMapper::toDto);
    }

    @Override
    public void update(PeerDto entity) {
        peerRepositoryImpl.update(peerMapper.toEntity(entity));
    }

    @Override
    public boolean delete(String id) {
        return peerRepositoryImpl.delete(id);
    }

    @Override
    public PeerDto save(PeerDto entity) {
        return peerMapper.toDto(peerRepositoryImpl.save(peerMapper.toEntity(entity)));
    }

    @Override
    public List<PeerDto> findByAll() {
        return peerRepositoryImpl.findByAll()
                .stream()
                .map(peerMapper::toDto)
                .toList();
    }
}
