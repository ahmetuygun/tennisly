package com.tennisly.club.service.impl;

import com.tennisly.club.domain.Player;
import com.tennisly.club.domain.User;
import com.tennisly.club.repository.PlayerRepository;
import com.tennisly.club.repository.UserRepository;
import com.tennisly.club.security.SecurityUtils;
import com.tennisly.club.service.PlayerService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Player}.
 */
@Service
@Transactional
public class PlayerServiceImpl implements PlayerService {

    private final Logger log = LoggerFactory.getLogger(PlayerServiceImpl.class);

    private final PlayerRepository playerRepository;

    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public Player save(Player player) {
        log.debug("Request to save Player : {}", player);
        return playerRepository.save(player);
    }

    @Override
    public Optional<Player> partialUpdate(Player player) {
        log.debug("Request to partially update Player : {}", player);

        return playerRepository
            .findById(player.getId())
            .map(existingPlayer -> {
                if (player.getFullName() != null) {
                    existingPlayer.setFullName(player.getFullName());
                }
                if (player.getGender() != null) {
                    existingPlayer.setGender(player.getGender());
                }
                if (player.getLevel() != null) {
                    existingPlayer.setLevel(player.getLevel());
                }
                if (player.getPhone() != null) {
                    existingPlayer.setPhone(player.getPhone());
                }
                if (player.getPhoto() != null) {
                    existingPlayer.setPhoto(player.getPhoto());
                }
                if (player.getPhotoContentType() != null) {
                    existingPlayer.setPhotoContentType(player.getPhotoContentType());
                }
                if (player.getStatus() != null) {
                    existingPlayer.setStatus(player.getStatus());
                }

                return existingPlayer;
            })
            .map(playerRepository::save);
    }

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<Player> findAll(Pageable pageable) {
        Optional<String> login = SecurityUtils.getCurrentUserLogin();
        User user = userRepository.findOneByLogin(login.orElseThrow()).orElseThrow();
        List<Player> players = playerRepository
            .findAll(pageable)
            .stream()
            .filter(player -> player.getInternalUser() != null && player.getInternalUser().getId() != user.getId())
            .collect(Collectors.toList());

        log.debug("Request to get all Players");

        return new PageImpl<>(players);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Player> findOne(Long id) {
        log.debug("Request to get Player : {}", id);
        return playerRepository.findById(id);
    }

    @Override
    public Optional<Player> findByLogin(String login) {
        log.debug("Request to get Player by login : {}", login);
        return playerRepository.findOneByInternalUser_Login(login);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Player : {}", id);
        playerRepository.deleteById(id);
    }
}
