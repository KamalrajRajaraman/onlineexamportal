package com.vastpro.constants;

/**
 * CommonContanst Interface contains all commonly used String Objects.
 * Note:
 * Any variable declared inside interface are by default public ,static and final 
 */
public interface CommonConstants {
	
	//Common Ofbiz constants
	public static final String RESULT ="result";
	public static final String SUCCESS = "success";
	public static final String ERROR = "error";
	public static final String RESPONSE_MESSAGE="responseMessage";
	public static final String USER_LOGIN_ID="userLoginId";
	public static final String PARTY_AND_USER_LOGIN ="PartyAndUserLogin";
	public static final String RESOURCE_ERROR = "OnlineexamUiLabels";
	public static final String _ERROR_MESSAGE_ ="_ERROR_MESSAGE_";
	public static final String HAS_FORM_ERROR ="hasFormErrors";
	public static final String RESULT_MAP ="resultMap";
	
	//Session Common Attribute 
	public static final String USER_LOGIN="userLogin";
	
	//Request Common Attribute
	public static final String DELEGATOR="delegator";
	public static final String 	DISPATCHER ="dispatcher";
	
	//Party Entity Constant 
	public static final String PARTY_ID = "partyId";
	public static final String PARTY_ROLE ="partyRole";
	public static final String PERSON_ROLE ="PERSON_ROLE";
	public static final String ADMIN ="ADMIN";
	
	public static final String FIRST_NAME ="firstName";
	public static final String LAST_NAME = "lastName";
	public static final String CURRENT_PASSWORD = "currentPassword";
	public static final String CURRENT_PASSWORD_VERIFY = "currentPasswordVerify";
	public static final String DISABLED_DATE_TIME ="disabledDateTime";
	
	
	
	//UserLogin Entity	
	public static final String USERNAME="USERNAME";
	public static final String PASSWORD="PASSWORD";
	
	
	
	
	 
	// ExamMaster Entity Constant
	public static final String EXAM_ID = "examId";
	public static final String EXAM_NAME = "examName";
	public static final String DESCRIPTION = "description";
	public static final String CREATION_DATE = "creationDate";
	public static final String EXPIRATION_DATE="expirationDate";
	public static final String NO_OF_QUESTIONS= "noOfQuestions";
	public static final String DURATION_MINUTES= "durationMinutes";
	public static final String PASS_PERCENTAGE= "passPercentage";
	public static final String QUESTIONS_RANDOMIZED="questionsRandomized";
	public static final String ANSWER_MUST="answersMust";
	public static final String ENABLE_NEGATIVE_MARK= "enableNegativeMark";
	public static final String NEGATIVE_MARK_VALUE="negativeMarkValue";
	
	
	//TopicMaster Entity Constant
	public static final String TOPIC_ID = "topicId";
	public static final String TOPIC_NAME = "topicName";
	
	
	
	//ExamTopicMappingMaster
	public static final String PERCENTAGE="percentage";
	public static final String TOPIC_PASS_PERCENTAGE = "topicPassPercentage";
	public static final String QUESTION_PER_EXAM="questionsPerExam";
	
	//---------------------------------QUESTION MASTER ENTITY CONSTANT----------------------------
	public static final String QUESTION_ID ="questionId";
	public static final String QUESTION_DETAIL ="questionDetail";
	public static final String OPTION_A ="optionA";
	public static final String OPTION_B ="optionB" ;
	public static final String OPTION_C ="optionC" ;
	public static final String OPTION_D ="optionD" ;
	public static final String OPTION_E ="optionE" ;
	public static final String ANSWER ="answer" ;
	public static final String NUM_ANSWERS ="numAnswers";
	public static final String QUESTION_TYPE ="questionType";
	public static final String DIFFICULTY_LEVEL ="difficultyLevel" ;
	public static final String ANSWER_VALUE ="answerValue" ;
	
	//--------------------------------------Entity Name Constant-------------------
	public static final String EXAM_MASTER ="ExamMaster";
	public static final String TOPIC_MASTER="TopicMaster";
	public static final String QUESTION_MASTER="QuestionMaster";
	public static final String EXAM_TOPIC_MAPPING_MASTER="ExamTopicMappingMaster";
	public static final String USER_ATTEMPT_MASTER="UserAttemptMaster";
	public static final String USER_ATTEMPT_TOPIC_MASTER="UserAttemptTopicMaster";
	public static final String USER_ATTEMPT_ANSWER_MASTER="UserAttemptAnswerMaster";
	public static final String USER_EXAM_MAPPING_MASTER="UserExamMappingMaster";
	public static final String USER_LOGIN_ENTITY="UserLogin";
	
	

	//--------------------------------------View Entity Name Constant-------------------
	public static final String USER_MASTER = "UserMaster";
	public static final String EXAM_TOPIC_MAPPING_VIEW_ENTITY = "ExamTopicMappingViewEntity";
	
	
	//--------------------------USER EXAM MAPPING ENTITY CONSTANT----------------------------
	public static final String ALLOWED_ATTEMPTS ="allowedAttempts";
	public static final String NO_OF_ATTEMPTS ="noOfAttempts";
	public static final String LAST_PERFORMANCE_DATE ="lastPerformanceDate";
	public static final String TIMEOUT_DAYS ="timeoutDays";
	public static final String PASSWORD_CHANGES_AUTO ="passwordChangesAuto";
	public static final String CAN_SPLIT_EXAMS ="canSplitExams";
	public static final String CAN_SEE_DETAILED_RESULTS ="canSeeDetailedResults";
	public static final String MAX_SPLIT_ATTEMPTS ="maxSplitAttempts";
	
	
	//---------------------------------------User Master View Entity-------------------------
	public static final String ROLE_TYPE_ID ="roleTypeId";
	
		
	//User Attempt Master
	public static final String PERFORMANCE_ID ="performanceId";
	public static final String ATTEMPT_NUMBER ="attemptNumber";	
	public static final String SCORE ="score";
	public static final String COMPLETED_DATE ="completedDate";
	public static final String TOTAL_CORRECT ="totalCorrect";
	public static final String TOTAL_WRONG ="totalWrong";
	public static final String USER_PASSED ="userPassed"; 
	
	//UserAttemptTopicMaster entity constant
	public static final String TOTAL_QUESTIONS_IN_THIS_TOPIC ="totalQuestionsInThisTopic";
	public static final String CORRECT_QUESTIONS_IN_THIS_TOPIC ="correctQuestionsInThisTopic";
	public static final String USER_TOPIC_PERCENTAGE ="userTopicPercentage";
	public static final String USER_PASSED_THIS_TOPIC ="userPassedThisTopic";
	
	
	//UserAttemptAnswerMaster
	public static final String SEQUENCE_NUM = "sequenceNum"; 
	public static final String SUBMITTED_ANSWER ="submittedAnswer";
	public static final String IS_FLAGGED ="isFlagged";	
	public static final String CREATE_EXAM="createExam";
	
	
	//constant 
	public static final String PARTY_ROLE_TYPE_ID="partyRoleTypeId";
	
	//Service names
	public static final String FIND_ALL_EXAMS = "findAllExams";
	public static final String FIND_EXAM_BY_ID= "findExamById";
	public static final String DELETE_EXAM="deleteExam";
	public static final String EDIT_EXAM = "editExam";
	public static final String UPDATE_TOPIC ="updateTopic";
	public static final String CREATE_TOPIC = "createTopic";
	public static final String FIND_ALL_TOPICS = "findAllTopics";
	public static final String FIND_TOPIC_BY_ID = "findTopicById";
	public static final String DELETE_TOPIC = "deleteTopic";
	public static final String UPDATE_QUESTION = "updateQuestion";
	public static final String CREATE_QUESTION = "createQuestion";
	public static final String FIND_QUESTION_BY_ID = "findQuestionById";
	public static final String FIND_ALL_QUESTIONS = "findAllQuestions";
	public static final String DELETE_QUESTION = "deleteQuestion";
	public static final String FIND_NO_OF_QUESTION_COUNT_BY_EXAM_ID = "findNoOfQuestionCountByExamId";
	public static final String UPDATE_EXAM_TOPIC_MAPPING_MASTER = "updateExamTopicMappingMaster";
	public static final String ADD_TOPIC_TO_EXAM = "addTopicToExam";
	public static final String CREATE_PERSON_AND_USER_LOGIN = "createPersonAndUserLogin";
	public static final String CREATE_PARTY_ROLE_RECORD = "createPartyRoleRecord";
	public static final String FIND_ALL_USER = "findAllUser";
	public static final String DELETE_USER_LOGIN = "deleteUserLogin";
	public static final String CREATE_USER_EXAM_MAPPING_RECORD ="createUserExamMappingRecord";
	public static final String UPDATE_USER_EXAM_MAPPING_RECORD = "updateUserExamMappingRecord";
	public static final String FIND_ALL_EXAM_FOR_PARTY_ID = "findAllExamForPartyId";
	public static final String FIND_USER_EXAM_MAPPING_RECORD = "findUserExamMappingRecord";
	public static final String CREATE_USER_ATTEMPT_MASTER = "createUserAttemptMaster";
	public static final String CREATE_USER_ATTEMPT_TOPIC_MASTER = "createUserAttemptTopicMaster";
	public static final String FIND_QUESTIONS_BY_TOPIC_IDS = "findQuestionsByTopicIds";
	public static final String CREATE_USER_ATTEMPT_ANSWER_MASTER_SERVICE = "createUserAttemptAnswerMasterService";
	public static final String UPDATE_USER_ATTEMPT_ANSWER_MASTER = "updateUserAttemptAnswerMaster";
	public static final String UPDATE_USER_ATTEMPT_TOPIC_MASTER = "updateUserAttemptTopicMaster";
	public static final String UPDATE_USER_ATTEMPT_MASTER = "updateUserAttemptMaster";
	
	
	
	//Event's constants
	public static final String EXAM = "exam";
	public static final String EXAM_LIST = "examList";
	public static final String TOPIC = "topic";
	public static final String TOPIC_LIST = "topicList";
	public static final String QUESTION = "question";
	public static final String QUESTION_LIST ="questionList";
	public static final String EXAM_TOPIC_MAPPING_MASTER_RECORD = "examTopicMappingMasterRecord";
	public static final String EXAM_TOPIC_MAPPING_RECORD_LIST = "examTopicMappingRecordList";
	public static final String PERSON_AND_USER_LOGIN_MAP = "personAndUserLoginMap";
	public static final String PARTY_ROLE_RECORD_MAP = "partyRoleRecordMap";
	public static final String USER_LIST = "userList";
	public static final String QUESTION_ID_LIST = "questionIdList";
	public static final String PERCENTAGE_TO_BE_UPDATED = "percentageToBeUpdated";
	public static final String PERCENTAGE_LIST = "percentageList";
	
	//Service's Constants
	public static final String CREATE_USER_EXAM_MAPPING_RECORD_MAP = "createUserExamMappingRecordMap";
	public static final String USER_EXAM_MAPPING_RECORD = "userExamMappingRecord";
	public static final String SELECTED_QUESTION = "selectedQuestion";
	public static final String SUBMITTED_QUESTION = "submittedQuestion";
	public static final String TOTAL_SCORE ="totalScore";
	public static final String TOTAL_WRONG_ANSWERS_IN_EXAM = "totalWrongAnswersInExam";
	public static final String TOTAL_CORRECT_QUESTIONS_IN_EXAM = "totalCorrectQuestionsInExam";
	public static final String USER_ATTEMPT_MASTER_LIST = "userAttemptMasterList";
	public static final String USER_ATTEMPT_TOPIC_MASTER_LIST = "userAttemptTopicMasterList";
	public static final String ACTUAL_USER_PERCENTAGE = "actualUserPercentage";
	public static final String NO_OF_CORRECTED_QUESTIONS_BY_TOPIC_ID = "noOfCorrectedQuestionsByTopicId";
	public static final String UPDATED_USER_ATTEMPT_TOPIC_MASTER_LIST = "updatedUserAttemptTopicMasterList";
	

}
