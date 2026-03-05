package com.pm.patientservice.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;

import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.exception.EmailAlreadyExistsException;
import com.pm.patientservice.exception.PatientNotFoundException;
import com.pm.patientservice.mapper.PatientMapper;
import com.pm.patientservice.model.patient;
import com.pm.patientservice.repository.PatientRepository;

@Service
public class PatientService {
    private PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<PatientResponseDTO> getPatients(){
        List<patient> patients = patientRepository.findAll();

        List<PatientResponseDTO> patientResponseDTOs =
            patients.stream().map(patient -> PatientMapper.toDTO(patient)).toList();

        return patientResponseDTOs;
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
        if(patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("A patient with this email: " + patientRequestDTO.getEmail() + " already exists.");
        }

        patient newPatient = patientRepository.save(
            PatientMapper.toModel(patientRequestDTO));

            return PatientMapper.toDTO(newPatient);
    }

    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO){
        
        patient patient = patientRepository.findById(id).orElseThrow(
            () -> new PatientNotFoundException("Patient not found with ID " + id));
            
            if(patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(), id)) {
                throw new EmailAlreadyExistsException("A patient with this email: " + patientRequestDTO.getEmail() + " already exists.");
            } 

            patient.setName(patientRequestDTO.getName());
            patient.setEmail(patientRequestDTO.getEmail());
            patient.setAddress(patientRequestDTO.getAddress());
            patient.setDateofBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));

            patient updatedPatient = patientRepository.save(patient);
            return PatientMapper.toDTO(updatedPatient);
    }

    @DeleteMapping
    public void deletePatient(UUID id){
        patientRepository.deleteById(id);
    }

}
