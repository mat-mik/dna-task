## Solution
1. decided to separate User and Job Offer
  - https://github.com/mat-mik/dna-task/tree/master/src/main/java/com/example/dnatask/user
  - https://github.com/mat-mik/dna-task/tree/master/src/main/java/com/example/dnatask/joboffer

These two modules definitely have different reasons for change, one domain (user) is heavily generic (replaceable with an off-the-shelf solution); the other domain looks like a core.

2. decided to extract Listing from JobOffer
https://github.com/mat-mik/dna-task/blob/34221523d73a803485e95ff08675314b0050fd7a/src/main/java/com/example/dnatask/joboffer/job/model/JobOffer.java#L16-L33

https://github.com/mat-mik/dna-task/blob/34221523d73a803485e95ff08675314b0050fd7a/src/main/java/com/example/dnatask/joboffer/listing/JobListing.java#L10-L18

It simplifies read model (less fields - no need for `startDate`, `endDate` or `employerId`), prepares domain to be rewritten with better tools **at the expense** of data synchronization.


## Missing parts / Improve area
- User's data (e.g. username/password). Only `name` was added. 
- Missing test cases - https://github.com/mat-mik/dna-task/blob/master/src/test/java/com/example/dnatask/joboffer/JobOfferIntegrationTest.java
- Synchronization Offer -> Listing code:
  - https://github.com/mat-mik/dna-task/blob/34221523d73a803485e95ff08675314b0050fd7a/src/main/java/com/example/dnatask/joboffer/job/JobOfferSynchronizationScheduler.java#L6
  - https://github.com/mat-mik/dna-task/blob/34221523d73a803485e95ff08675314b0050fd7a/src/main/java/com/example/dnatask/joboffer/job/JobOfferService.java#L64
  - https://github.com/mat-mik/dna-task/blob/34221523d73a803485e95ff08675314b0050fd7a/src/main/java/com/example/dnatask/joboffer/job/JobOfferService.java#L72
- Naive query model https://github.com/mat-mik/dna-task/blob/34221523d73a803485e95ff08675314b0050fd7a/src/main/java/com/example/dnatask/joboffer/listing/JobListingService.java#L39-L50
- Cleanup:
  - use more observable behaviour to do assertions https://github.com/mat-mik/dna-task/blob/34221523d73a803485e95ff08675314b0050fd7a/src/test/java/com/example/dnatask/user/UserServiceIntegrationTest.java#L125-L129
  - add missing `then` sections https://github.com/mat-mik/dna-task/blob/34221523d73a803485e95ff08675314b0050fd7a/src/test/java/com/example/dnatask/joboffer/JobOfferIntegrationTest.java#L128-L129
