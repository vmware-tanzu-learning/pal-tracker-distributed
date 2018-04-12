
flyway -url="jdbc:mysql://localhost:3306/tracker_allocations_dev" -locations=filesystem:databases/allocations-database clean migrate
flyway -url="jdbc:mysql://localhost:3306/tracker_allocations_test" -locations=filesystem:databases/allocations-database clean migrate

flyway -url="jdbc:mysql://localhost:3306/tracker_backlog_dev" -locations=filesystem:databases/backlog-database clean migrate
flyway -url="jdbc:mysql://localhost:3306/tracker_backlog_test" -locations=filesystem:databases/backlog-database clean migrate

flyway -url="jdbc:mysql://localhost:3306/tracker_registration_dev" -locations=filesystem:databases/registration-database clean migrate
flyway -url="jdbc:mysql://localhost:3306/tracker_registration_test" -locations=filesystem:databases/registration-database clean migrate


flyway -url="jdbc:mysql://localhost:3306/tracker_timesheets_dev" -locations=filesystem:databases/timesheets-database clean migrate
flyway -url="jdbc:mysql://localhost:3306/tracker_timesheets_test" -locations=filesystem:databases/timesheets-database clean migrate


