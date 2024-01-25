package nst.springboot.nstapplication.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import nst.springboot.nstapplication.converter.impl.AcademicTitleConverter;
import nst.springboot.nstapplication.converter.impl.AcademicTitleHistoryConverter;
import nst.springboot.nstapplication.converter.impl.MemberConverter;
import nst.springboot.nstapplication.converter.impl.ScientificFieldConverter;
import nst.springboot.nstapplication.domain.*;
import nst.springboot.nstapplication.dto.AcademicTitleHistoryDto;
import nst.springboot.nstapplication.exception.EntityNotFoundException;
import nst.springboot.nstapplication.exception.IllegalArgumentException;
import nst.springboot.nstapplication.repository.AcademicTitleHistoryRepository;
import nst.springboot.nstapplication.repository.AcademicTitleRepository;
import nst.springboot.nstapplication.repository.MemberRepository;
import nst.springboot.nstapplication.repository.ScientificFieldRepository;
import nst.springboot.nstapplication.service.AcademicTitleHistoryService;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AcademicTitleHistoryServiceImpl implements AcademicTitleHistoryService {
    private final AcademicTitleHistoryConverter academicTitleHistoryConverter;
    private final AcademicTitleHistoryRepository academicTitleHistoryRepository;
    private final MemberRepository memberRepository;
    private final MemberConverter memberConverter;
    private final ScientificFieldConverter scientificFieldConverter;
    private final AcademicTitleConverter academicTitleConverter;
    private final AcademicTitleRepository academicTitleRepository;
    private final ScientificFieldRepository scientificFieldRepository;

    @Override
    @Transactional
    public AcademicTitleHistoryDto save(AcademicTitleHistoryDto academicTitleDTO) {
        if(academicTitleDTO.getEndDate()!=null && academicTitleDTO.getStartDate()!= null){
            if(academicTitleDTO.getEndDate().isBefore(academicTitleDTO.getStartDate())){
                throw new IllegalArgumentException("End date can't be before start date!");
            }
        }
        if(academicTitleDTO.getStartDate().isAfter(LocalDate.now())){
            throw new IllegalArgumentException("Can't set academic title for the future! Date can't be after today date!");
        }

        Optional<Member> existingMember = null;
        if(academicTitleDTO.getMember().getId()!=null){
            existingMember = memberRepository.findById(academicTitleDTO.getMember().getId());
            if(existingMember.isPresent()){
                academicTitleDTO.setMember(memberConverter.toDto(existingMember.get()));
            }
            else{
                throw new EntityNotFoundException("There is no member with that id!");
            }
        }
        Optional<AcademicTitle> existingAcademicTitle;
        if(academicTitleDTO.getAcademicTitle().getId()!=null){
            existingAcademicTitle = academicTitleRepository.findById(academicTitleDTO.getAcademicTitle().getId());
            if(existingAcademicTitle.isPresent()){
                academicTitleDTO.setAcademicTitle(academicTitleConverter.toDto(existingAcademicTitle.get()));
            }
            else{
                throw new EntityNotFoundException("There is no academic title with that id!");
            }
            if(existingAcademicTitle.get().getName().equals(academicTitleDTO.getMember().getAcademicTitle().getName())){
                throw new IllegalArgumentException("Member "+academicTitleDTO.getMember().getFirstname()+ " "+
                        academicTitleDTO.getMember().getLastname()+
                        " is already "+
                        academicTitleDTO.getAcademicTitle().getName());
            }
        }
        Optional<ScientificField> existingScientificField;
        if(academicTitleDTO.getScientificField().getId()!=null){
            existingScientificField = scientificFieldRepository.findById(academicTitleDTO.getScientificField().getId());
            if(existingScientificField.isPresent()){
                if(!existingScientificField.get().getName().equals(academicTitleDTO.getMember().getScientificField().getName())){
                    throw new IllegalArgumentException("Scientific field of member is "+ academicTitleDTO.getMember().getScientificField().getName()
                    +". You didn't provided right scientific field!");
                }
                academicTitleDTO.setScientificField(scientificFieldConverter.toDto(existingScientificField.get()));
            }
            else{
                throw new EntityNotFoundException("There is no scientific field with that id!");
            }
        }
        Optional<AcademicTitleHistory> ac = academicTitleRepository.findCurrentAcademicTitleByMemberId(academicTitleDTO.getMember().getId());
        if(ac.isPresent()){
            if(academicTitleDTO.getStartDate().isBefore(ac.get().getStartDate()) && academicTitleDTO.getEndDate()==null){
                throw new IllegalArgumentException("Actual academic title for member "+
                        academicTitleDTO.getMember().getFirstname()+" "+
                        academicTitleDTO.getMember().getLastname()+" is "+
                        academicTitleDTO.getAcademicTitle().getName());
            }
        }
        List<AcademicTitleHistory> academicTitleHistoryList = academicTitleHistoryRepository.findAllByMemberIdAndEndDateNotNull(academicTitleDTO.getMember().getId());
        for(AcademicTitleHistory academicTitle : academicTitleHistoryList){
            if(academicTitleDTO.getEndDate() !=null){
                if(isDateOverlap(academicTitleDTO.getStartDate(), academicTitleDTO.getEndDate(), academicTitle.getStartDate(), academicTitle.getEndDate())){
                    throw new IllegalArgumentException("Member "+ academicTitleDTO.getMember().getFirstname() +" "+academicTitleDTO.getMember().getLastname()+
                            " has been "+academicTitle.getAcademicTitle().getName()+" from "+academicTitle.getStartDate()+" to "+academicTitle.getEndDate());
                }
            }
        }
        Optional<AcademicTitleHistory> academicTitleHistory = academicTitleHistoryRepository.findCurrentAcademicTitleByMemberId(academicTitleDTO.getMember().getId());
        if(academicTitleHistory.isPresent()){
                if(academicTitleDTO.getStartDate().isAfter(LocalDate.now())){
                    throw new IllegalArgumentException("Can't set academic title for the future! Date can't be after today date!");
                }
                if(academicTitleDTO.getEndDate()!=null && academicTitleDTO.getEndDate().isAfter(academicTitleHistory.get().getStartDate())){
                    academicTitleHistory.get().setEndDate(academicTitleDTO.getStartDate());
                    update(academicTitleHistory.get().getId(),academicTitleHistoryConverter.toDto(academicTitleHistory.get()));
                    academicTitleHistoryRepository.save(academicTitleHistory.get());
                    existingMember.get().setAcademicTitle(academicTitleConverter.toEntity(academicTitleDTO.getAcademicTitle()));
                    memberRepository.save(existingMember.get());
                }
                if(academicTitleDTO.getEndDate()==null && academicTitleHistory.get().getStartDate().isBefore(academicTitleDTO.getStartDate())){
                    academicTitleHistory.get().setEndDate(academicTitleDTO.getStartDate());
                    update(academicTitleHistory.get().getId(),academicTitleHistoryConverter.toDto(academicTitleHistory.get()));
                    academicTitleHistoryRepository.save(academicTitleHistory.get());
                    existingMember.get().setAcademicTitle(academicTitleConverter.toEntity(academicTitleDTO.getAcademicTitle()));
                    memberRepository.save(existingMember.get());
                }
        }
        if(!academicTitleDTO.getStartDate().isBefore(LocalDate.now()) && !academicTitleDTO.getEndDate().isBefore(LocalDate.now())){
            existingMember.get().setAcademicTitle(academicTitleConverter.toEntity(academicTitleDTO.getAcademicTitle()));
            memberRepository.save(existingMember.get());
        }

        return academicTitleHistoryConverter.toDto(academicTitleHistoryRepository.save(academicTitleHistoryConverter.toEntity(academicTitleDTO)));
    }
    private boolean isDateOverlap(LocalDate startDate1, LocalDate endDate1, LocalDate startDate2, LocalDate endDate2) {
        return startDate1.isBefore(endDate2) && endDate1.isAfter(startDate2);
    }
    @Override
    public List<AcademicTitleHistoryDto> getAll() {
        return academicTitleHistoryRepository
                .findAll()
                .stream().map(entity -> academicTitleHistoryConverter.toDto(entity))
                .collect(Collectors.toList());
    }

    @Override
    public AcademicTitleHistoryDto findById(Long id) {
        Optional<AcademicTitleHistory> title = academicTitleHistoryRepository.findById(id);
        if (title.isPresent()) {
            AcademicTitleHistory academicTitle = title.get();
            return academicTitleHistoryConverter.toDto(academicTitle);
        } else {
            throw new EntityNotFoundException("Academic title history does not exist!");
        }
    }
    @Override
    public List<AcademicTitleHistoryDto> findByMemberId(Long id) {
        List<AcademicTitleHistory>  academicTitleHistoryList= academicTitleHistoryRepository.findAllByMemberIdOrderByStartDateDesc(id);
        List<AcademicTitleHistoryDto> academicTitleHistoryDtoList= new ArrayList<>();
        if (!academicTitleHistoryList.isEmpty()){
            for (AcademicTitleHistory ac : academicTitleHistoryList) {
                academicTitleHistoryDtoList.add(academicTitleHistoryConverter.toDto(ac));
            }
        } else {
            throw new EntityNotFoundException("Academic title history does not exist!");
        }
        return academicTitleHistoryDtoList;
    }

    @Override
    public AcademicTitleHistoryDto update(Long id, AcademicTitleHistoryDto academicTitleHistoryDto) {
        Optional<AcademicTitleHistory> academicTitleHistory = academicTitleHistoryRepository.findById(id);

        if (academicTitleHistory.isPresent()){
            AcademicTitleHistory existingTitle = academicTitleHistoryRepository.findById(id).get();
              existingTitle.setAcademicTitle(academicTitleHistory.get().getAcademicTitle());
              existingTitle.setEndDate(academicTitleHistory.get().getEndDate());
              existingTitle.setMember(academicTitleHistory.get().getMember());
              existingTitle.setStartDate(academicTitleHistory.get().getStartDate());
              existingTitle.setScientificField(academicTitleHistory.get().getScientificField());
            AcademicTitleHistory updatedTitle = academicTitleHistoryRepository.save(existingTitle);

            return AcademicTitleHistoryDto.builder().
                    id(updatedTitle.getId()).
                    member(memberConverter.toDto(updatedTitle.getMember())).
                    scientificField(scientificFieldConverter.toDto(updatedTitle.getScientificField())).
                    academicTitle(academicTitleConverter.toDto(updatedTitle.getAcademicTitle())).
                    endDate(updatedTitle.getEndDate()).
                    startDate(updatedTitle.getStartDate()).
                    build();

        }else{
            throw new EntityNotFoundException("Academic title history not found with id: " + id);
        }

    }

}
