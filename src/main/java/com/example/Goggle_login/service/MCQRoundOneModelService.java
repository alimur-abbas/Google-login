package com.example.Goggle_login.service;

import com.example.Goggle_login.dao.MCQRoundOneDao;
import com.example.Goggle_login.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MCQRoundOneModelService {
    @Autowired
    private MCQRoundOneDao mcqRoundOneDao;

    private static final String CSV_FILE_PATH = "C:\\Users\\Alimur\\Desktop\\SpringBoot\\mcq-round-1\\src\\main\\resources\\mcq.csv";

    public List<ExaminationModel> readCsv() throws IOException {
        FileReader fileReader = new FileReader(CSV_FILE_PATH);
        CSVReader csvReader = new CSVReaderBuilder(fileReader).withSkipLines(1).build();
        final ExaminationModel examinationModel = new ExaminationModel();
        List<ExaminationModel> examinationModelList = csvReader.readAll().stream().map(data -> {
            if (examinationModel.getExamCode() == null) {
                examinationModel.setExaminationId(UUID.randomUUID().toString());
                examinationModel.setExamCode(data[0]);
//             System.out.println(examinationModel.setExamCode(Integer.parseInt(data[1])));
                examinationModel.setExamTitle(data[1]);
                examinationModel.setExamDesc(data[2]);
                examinationModel.setExamDate(data[3]);
                examinationModel.setExamTime(data[4]);
                examinationModel.setExamDuration(data[5]);
                examinationModel.setQuestionModelList(new ArrayList<>());
            }

            List<QuestionModel> questionModelList = examinationModel.getQuestionModelList();

            QuestionModel questionModel = new QuestionModel();
            questionModel.setExaminationId(examinationModel.getExaminationId());
            questionModelList.add(questionModel);
            questionModel.setQuestionLabel(data[6]);
            questionModel.setQuestionDescription(data[7]);
            questionModel.setAnswerLabel(data[8]);
            questionModel.setQuestionId(UUID.randomUUID().toString());
            questionModel.setOptionModelList(new ArrayList<>());

            List<OptionModel> optionModelList = questionModel.getOptionModelList();


            OptionModel optionModel = new OptionModel();
            optionModel.setOptionId(UUID.randomUUID().toString());
            optionModel.setQuestionId(questionModel.getQuestionId());
            optionModel.setOptionLabel(data[9]);
            optionModel.setOptionDescription(data[13]);

            OptionModel optionModel1 = new OptionModel();
            optionModel1.setOptionId(UUID.randomUUID().toString());
            optionModel1.setQuestionId(questionModel.getQuestionId());
            optionModel1.setOptionLabel(data[10]);
            optionModel1.setOptionDescription(data[14]);

            OptionModel optionModel2 = new OptionModel();
            optionModel2.setOptionId(UUID.randomUUID().toString());
            optionModel2.setQuestionId(questionModel.getQuestionId());
            optionModel2.setOptionLabel(data[11]);
            optionModel2.setOptionDescription(data[15]);

            OptionModel optionModel3 = new OptionModel();
            optionModel3.setOptionId(UUID.randomUUID().toString());
            optionModel3.setQuestionId(questionModel.getQuestionId());
            optionModel3.setOptionLabel(data[12]);
            optionModel3.setOptionDescription(data[16]);

            optionModelList.add(optionModel);
            optionModelList.add(optionModel1);
            optionModelList.add(optionModel2);
            optionModelList.add(optionModel3);


            return examinationModel;
        }).collect(Collectors.toList());
//        examinationModelList.forEach(System.out::println);
        return examinationModelList;

    }
    public void save(ExaminationModel ex){
        mcqRoundOneDao.insertCsvData(ex);
    }

    public ExaminationModel getExamModel(String examCode){
        ExaminationModel exModel = mcqRoundOneDao.selectExamModel(examCode);
        return exModel;
    }

    public UserExamAttempt saveAttempt(String examId){
        UserExamAttempt userExamAttempt = new UserExamAttempt();
        userExamAttempt.setAttemptId(UUID.randomUUID().toString());
        userExamAttempt.setExamId(examId);
        userExamAttempt.setAccountId("user");
        userExamAttempt.setExamType(ExamType.RETAKE);
        long startTime = System.currentTimeMillis();
        userExamAttempt.setStartedAt(startTime);
        userExamAttempt.setEndAt(startTime + 3600000);
        mcqRoundOneDao.insertUserAttemptDetails(userExamAttempt);
        return userExamAttempt;
    }

    public List<QuestionModel> getQuestionModel(String examId){
        return mcqRoundOneDao.selectQuestionModel(examId);
    }

    public List<OptionModel> getOptionModel(String questionId){
        return  mcqRoundOneDao.selectOptionModel(questionId);
    }

    public void saveUserAnswer(UserAnswerModel userAnswerModel){
        mcqRoundOneDao.insertUserAnswer(userAnswerModel);
    }

    public List<UserExamResult> getUserResult(UserResultQueryRequest userResultQueryRequest) throws JsonProcessingException {
        return mcqRoundOneDao.viewUserResult(userResultQueryRequest);
    }
}
