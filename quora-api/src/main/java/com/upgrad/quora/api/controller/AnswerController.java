package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.AnswerBusinessService;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
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

    @RequestMapping(method = RequestMethod.PUT, path = "/answer/edit/{answerId}",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerEditResponse> editAnswerContent(@RequestHeader("authorization") final String authorization,
                                                                @PathVariable("answerId") final String answerId,
                                                                final AnswerEditRequest answerEditRequest)
            throws AuthorizationFailedException, AnswerNotFoundException {

        /* Call the editAnswerContent from service to update the answer with given content */
        String answerContent = answerEditRequest.getContent();
        AnswerEntity updatedAnswer = answerBusinessService.editAnswerContent(answerId,
                answerContent, authorization);

        /* Once the answer is updated, prepare the response with UUID and a status message */
        AnswerEditResponse answerEditResponse = new AnswerEditResponse();
        answerEditResponse.id(updatedAnswer.getUuid()).status("ANSWER EDITED");
        /* return the response object back to the client*/
        return new ResponseEntity<AnswerEditResponse>(answerEditResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/answer/delete/{answerId}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerDeleteResponse> deleteAnswer(@RequestHeader("authorization") final String authorization,
                                                             @PathVariable("answerId") final String answerId)
            throws AuthorizationFailedException, AnswerNotFoundException {

        /* Call the deleteAnswer from service to delete the answer*/
        AnswerEntity deletedAnswer = answerBusinessService.deleteAnswer(answerId, authorization);

        /* Once the answer is deleted, prepare the response with UUID and a status message */
        AnswerDeleteResponse answerDeleteResponse = new AnswerDeleteResponse();
        answerDeleteResponse.id(deletedAnswer.getUuid()).status("ANSWER DELETED");
        /* return the response object back to the client*/
        return new ResponseEntity<AnswerDeleteResponse>(answerDeleteResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "answer/all/{questionId}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<AnswerDetailsResponse>> getAllAnswersToQuestion (@RequestHeader("authorization") final String authorization,
                                                                         @PathVariable("questionId") final String questionId)
            throws AuthorizationFailedException, InvalidQuestionException {
        /* Get the list of all the answer to given question from database if the authorization done successfully. */
        List<AnswerEntity> answerEntities = answerBusinessService.getAllAnswersToQuestion(authorization, questionId);

        /* Prepare the response with the required details from database and create a response list */
        List<AnswerDetailsResponse> answerResponseList = new LinkedList<AnswerDetailsResponse>();
        for (AnswerEntity answer : answerEntities) {
            AnswerDetailsResponse answerDetailsResponse = new AnswerDetailsResponse();
            answerDetailsResponse.id(answer.getUuid()).answerContent(answer.getAnswer()).questionContent(answer.getQuestion().getContent());
            answerResponseList.add(answerDetailsResponse);
        }
        /* Return the details of questions in the form of responseList and a Httpstatus.OK to client */
        return new ResponseEntity<List<AnswerDetailsResponse>>(answerResponseList, HttpStatus.OK);
    }
}
