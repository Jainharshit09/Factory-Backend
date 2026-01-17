package com.factory.repository;

import com.factory.model.MachineEvent;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MachineEventRepository extends MongoRepository<MachineEvent, String> {
}
