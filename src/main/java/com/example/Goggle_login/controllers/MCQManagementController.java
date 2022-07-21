package com.example.Goggle_login.controllers;

import com.example.Goggle_login.dao.LoginDaoImpl;
import com.example.Goggle_login.model.*;
import com.example.Goggle_login.service.MCQRoundOneModelService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
public class MCQManagementController {
    @Autowired
    private MCQRoundOneModelService mcqRoundOneModelService;
    @Autowired
    private LoginDaoImpl loginDao;

    @GetMapping("/read-csv")
    public void readCsv() throws IOException {
        List<ExaminationModel> list = mcqRoundOneModelService.readCsv();
        System.out.println("executed");
        mcqRoundOneModelService.save(list.get(0));
    }

    @GetMapping("/detail/{exam_code}")
    public ResponseEntity<UserExamAttemptWrapper> getModel(@PathVariable String exam_code) {
        System.out.println("hii");
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
        System.out.println("/save-user-answer");
        mcqRoundOneModelService.saveUserAnswer(ua);
        return ResponseEntity.ok(ua);
    }

    @PostMapping("/result")
    public ResponseEntity<List<UserExamResult>> userResult(@RequestBody UserResultQueryRequest ur) throws JsonProcessingException {
        List<UserExamResult> result = mcqRoundOneModelService.getUserResult(ur);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/validate/jwt")
    public Map<String,String> getParams(@RequestParam("uuid") String uuid){
        Map<String,String> map = new HashMap();
        JwtToken jwtToken = loginDao.getTokenFromUuid(uuid);
        System.out.println(uuid);
        System.out.println(jwtToken.getToken());
        map.put("token", jwtToken.getToken());
        return map;
    }


}

