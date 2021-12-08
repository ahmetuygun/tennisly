package com.tennisly.club.service.impl;

import com.tennisly.club.domain.Cord;
import com.tennisly.club.repository.CordRepository;
import com.tennisly.club.service.CordService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Cord}.
 */
@Service
@Transactional
public class CordServiceImpl implements CordService {

    private final Logger log = LoggerFactory.getLogger(CordServiceImpl.class);

    private final CordRepository cordRepository;

    public CordServiceImpl(CordRepository cordRepository) {
        this.cordRepository = cordRepository;
    }

    @Override
    public Cord save(Cord cord) {
        log.debug("Request to save Cord : {}", cord);
        return cordRepository.save(cord);
    }

    @Override
    public Optional<Cord> partialUpdate(Cord cord) {
        log.debug("Request to partially update Cord : {}", cord);

        return cordRepository
            .findById(cord.getId())
            .map(existingCord -> {
                if (cord.getName() != null) {
                    existingCord.setName(cord.getName());
                }
                if (cord.getAdress() != null) {
                    existingCord.setAdress(cord.getAdress());
                }
                if (cord.getImage() != null) {
                    existingCord.setImage(cord.getImage());
                }
                if (cord.getImageContentType() != null) {
                    existingCord.setImageContentType(cord.getImageContentType());
                }
                if (cord.getStatus() != null) {
                    existingCord.setStatus(cord.getStatus());
                }

                return existingCord;
            })
            .map(cordRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cord> findAll() {
        log.debug("Request to get all Cords");
        return cordRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cord> findOne(Long id) {
        log.debug("Request to get Cord : {}", id);
        return cordRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Cord : {}", id);
        cordRepository.deleteById(id);
    }
}
