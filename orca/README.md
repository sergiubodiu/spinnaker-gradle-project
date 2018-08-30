This project reads text files (output of some process) and saves them into in-memory H2 database. Contains task definitions for running Bakery tasks with Orca.

To run the output processing job
---------------------------------

Open Swagger (http://localhost:8090/swagger-ui.html):
Run the end point - /api/v1/batches/process/outputs (processPricingEngineOutputsJob method) with the following data:

executionID - 10 (could be any number)
working directory - C:\Temp\Main (Extract the zip file in the Main folder, dont specify Validus here. C:\Temp could be anything on your drive)
models = Validus

To see the running jobs
-----------------------
Open Swagger (http://localhost:8090/swagger-ui.html):
Run the end point - /api/v1/batches/jobs/running
Notice, output will be an empty response

Important Classes:

JobConfig
BatchJobLauncher
The two controllers in question are OutputProcessingController and MainBatchController
getAllRunningJobs() is in the MainBatchController which doesn't return anything and is the problem.





