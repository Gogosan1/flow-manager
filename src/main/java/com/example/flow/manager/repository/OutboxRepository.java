package com.example.flow.manager.repository;

import com.example.flow.manager.model.entity.Outbox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OutboxRepository extends JpaRepository<Outbox, UUID> {
}