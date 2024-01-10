package com.vastpro.constants;

/**
 * CommonContanst Interface contains all commonly used String Objects.
 * Note:
 * Any variable declared inside interface are by default public ,static and final
 * 
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

	//--------------------------------------View Entity Name Constant-------------------
	public static final String USER_MASTER = "UserMaster";
	
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
	
}
