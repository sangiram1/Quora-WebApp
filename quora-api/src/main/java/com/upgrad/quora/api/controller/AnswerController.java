package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.AnswerRequest;
import com.upgrad.quora.api.model.AnswerResponse;
import com.upgrad.quora.service.business.AnswerBusinessService;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/")
public class AnswerController {

    @Autowired
    private AnswerBusinessService answerBusinessService;

    @RequestMapping(method = RequestMethod.POST, path = "/question/{questionId}/answer/create",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerResponse> createAnswer(@PathVariable("questionId") final String questionId,
                                                       @RequestHeader("authorization") final String authorization,
                                                       final AnswerRequest answerRequest)
            throws AuthorizationFailedException, InvalidQuestionException {

        /* Prepare the answerEntity object with the data fed by the user */
        AnswerEntity answerEntity = new AnswerEntity();
        answerEntity.setUuid(UUID.randomUUID().toString());
        answerEntity.setAnswer(answerRequest.getAnswer());

        final AnswerEntity createdAnswerEntity = answerBusinessService.createAnswer(answerEntity, questionId, authorization);

        AnswerResponse response = new AnswerResponse().id(createdAnswerEntity.getUuid()).status("ANSWER CREATED");
        return new ResponseEntity<AnswerResponse>(response, HttpStatus.CREATED);

    }
}
