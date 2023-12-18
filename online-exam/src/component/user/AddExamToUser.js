import React from "react";

const AddExamToUser = () => {
  const handleSumbit = (e) => {
    e.preventDefault();
  };
  return (
    <div className="fluid-container mt-2  fw-bold">
      <form onSubmit={handleSumbit}>
        <div className="row col-10 mx-auto pt-2 border ">
          <div className="col">
            {" "}
            <div class="mb-3">
              <label for="partyId" class="form-label">
                partyId
              </label>
              <input
                type="text"
                class="form-control"
                id="partyId"
                name="partyId"
              />
            </div>
            <div class="mb-3">
              <label for="examId" class="form-label">
                examId
              </label>
              <input
                type="text"
                class="form-control"
                id="examId"
                name="examId"
              />
            </div>
            <div class="mb-3">
              <label for="allowedAttempts" class="form-label">
                allowedAttempts
              </label>
              <input
                type="text"
                class="form-control"
                id="allowedAttempts"
                name="allowedAttempts"
              />
            </div>
            <div class="mb-3">
              <label for="noOfAttempts" class="form-label">
                noOfAttempts
              </label>
              <input
                type="text"
                class="form-control"
                id="noOfAttempts"
                name="noOfAttempts"
              />
            </div>
            <div class="mb-3 col-4">
              <label for="lastPerformanceDate" class="form-label">
                lastPerformanceDate
              </label>
              <input
                type="date"
                class="form-control"
                id="lastPerformanceDate"
                name="lastPerformanceDate"
              />
            </div>
          </div>
          <div className="col">
            <div class="mb-3">
              <label for="timeoutDays" class="form-label">
                timeoutDays
              </label>
              <input
                type="text"
                class="form-control"
                id="timeoutDays"
                name="timeoutDays"
              />
            </div>

            <div className="mb-3 col-3">
              <label htmlFor="passwordChangesAuto" className="form-label">
                passwordChangesAuto
              </label>
              <select
                className="form-control"
                id="passwordChangesAuto"
                name="passwordChangesAuto"
                defaultValue={"Y"}
              >
                <option value="Y">Yes</option>
                <option value="N">NO</option>
              </select>
            </div>

            <div className="mb-3 col-3">
              <label htmlFor="canSplitExams" className="form-label">
                canSplitExams
              </label>
              <select
                className="form-control"
                id="canSplitExams"
                name="canSplitExams"
                defaultValue={"Y"}
              >
                <option value="Y">Yes</option>
                <option value="N">NO</option>
              </select>
            </div>

            <div className="mb-3 col-3">
              <label htmlFor="canSeeDetailedResults" className="form-label">
                canSeeDetailedResults
              </label>
              <select
                className="form-control"
                id="canSeeDetailedResults"
                name="canSeeDetailedResults"
                defaultValue={"Y"}
              >
                <option value="Y">Yes</option>
                <option value="N">NO</option>
              </select>
            </div>

            <div class="mb-3">
              <label for="maxSplitAttempts" class="form-label">
                maxSplitAttempts
              </label>
              <input
                type="text"
                class="form-control"
                id="maxSplitAttempts"
                name="maxSplitAttempts"
              />
            </div>
          </div>
        </div>
      </form>
    </div>
  );
};

// partyId varchar(20) NOT NULL,
// examId varchar(20) NOT NULL,
// allowedAttempts decimal(10,0) NOT NULL default '1',
// noOfAttempts decimal(10,0) NOT NULL default '0',
// lastPerformanceDate datetime default NULL,
// timeoutDays decimal(10,0) NOT NULL default '30',
// passwordChangesAuto char(1) default ‘Y’,
// canSplitExams char(1) default ‘Y’,
// canSeeDetailedResults char(1) default ‘Y’,
// maxSplitAttempts int(10) unsigned default '0',
// PRIMARY KEY (partyId,examId),

export default AddExamToUser;
