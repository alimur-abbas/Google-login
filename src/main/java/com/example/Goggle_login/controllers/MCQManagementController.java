package com.example.Goggle_login.controllers;

import com.example.Goggle_login.model.*;
import com.example.Goggle_login.service.MCQRoundOneModelService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@RestController
public class MCQManagementController {
    @Autowired
    private MCQRoundOneModelService mcqRoundOneModelService;

    @GetMapping("/read-csv")
    public void readCsv() throws IOException {
        List<ExaminationModel> list = mcqRoundOneModelService.readCsv();
        mcqRoundOneModelService.save(list.get(0));
    }

    @GetMapping("/detail/{exam_code}")
    public ResponseEntity<UserExamAttemptWrapper> getModel(@PathVariable String exam_code) {
        ExaminationModel ex = mcqRoundOneModelService.getExamModel(exam_code);
        UserExamAttempt userExamAttempt = mcqRoundOneModelService.saveAttempt(ex.getExaminationId());
        List<QuestionModel> list = mcqRoundOneModelService.getQuestionModel(ex.getExaminationId());

        ex.setQuestionModelList(list);
        List<OptionModel> optionModelList;
        for (QuestionModel op : list) {
            optionModelList = mcqRoundOneModelService.getOptionModel(op.getQuestionId());
            op.setOptionModelList(optionModelList);
        }
        UserExamAttemptWrapper userExamAttemptWrapper = new UserExamAttemptWrapper();
        userExamAttemptWrapper.setExaminationModel(ex);
        userExamAttemptWrapper.setUserExamAttempt(userExamAttempt);
        return ResponseEntity.ok(userExamAttemptWrapper);
    }

    @PostMapping("/save-user-answer")
    public ResponseEntity<UserAnswerModel> createUserAnswer(@RequestBody UserAnswerModel ua) {
        mcqRoundOneModelService.saveUserAnswer(ua);
        return ResponseEntity.ok(ua);
    }

    @PostMapping("/result")
    public ResponseEntity<List<UserExamResult>> userResult(@RequestBody UserResultQueryRequest ur) throws JsonProcessingException {
        List<UserExamResult> result = mcqRoundOneModelService.getUserResult(ur);
        return ResponseEntity.ok(result);
    }


}

