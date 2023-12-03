package com.fersko.info.service;

import com.fersko.info.entity.Peer;
import com.fersko.info.repository.PeerRepository;

import java.util.List;
import java.util.Optional;

public class PeerService implements BaseService<Peer, String> {

    private final PeerRepository peerRepository;

    public PeerService(PeerRepository peerRepository) {
        this.peerRepository = peerRepository;
    }

    @Override
    public Optional<Peer> findById(String id) {
        return peerRepository.findById(id);
    }

    @Override
    public void update(Peer entity) {
        peerRepository.update(entity);
    }

    @Override
    public boolean delete(String id) {
        return peerRepository.delete(id);
    }

    @Override
    public Peer save(Peer entity) {
        return peerRepository.save(entity);
    }

    @Override
    public List<Peer> findByAll() {
        return peerRepository.findByAll();
    }
}
