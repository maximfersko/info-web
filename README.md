# info

REST API server for a peer-review tracking system (school project data: peers, tasks, checks, friendships). Built on raw Jakarta Servlet API with manual JDBC - no ORM, no Spring.

## Tech Stack

- Java 17, Jakarta Servlet API 5.0, embedded Tomcat 10.1
- PostgreSQL + HikariCP connection pool
- Jackson (`jackson-datatype-jsr310` for `LocalDate` support)
- Lombok, JUnit 5, Mockito
- Maven, packaged as WAR (`com.fersko.info.Main`)

## Notable Implementation Details

- **No framework, no ORM.** All HTTP routing is done via `@WebServlet` annotations; SQL is written by hand with `PreparedStatement`. The 4-layer stack (entity - repository - service - servlet) is wired manually via constructors, which makes Mockito injection straightforward.
- **Generic service/repository interfaces.** `BaseRepository<T>` and `BaseService<T extends BaseDto>` define the full CRUD contract once; all 4 entities implement it with zero code duplication in the interface layer.
- **`ResponseHandler` utility class** centralizes all servlet response logic: `handleGetRequest`, `handleDeleteRequest`, and `sendJsonResponse` are static helpers shared across all 4 servlets, keeping each servlet's HTTP methods to 3-5 lines.
- **Recursive self-join mapping for `Task`.** `Task` has a nullable `parentTask` of the same type. `TaskMapper.toDto()` recurses into itself for the parent, and `TaskRepositoryImpl` resolves the join via a `LEFT JOIN tasks t1 ON t.parent_task = t1.pk_title` query with aliased columns (`parent_task_title`, `p_task_xp`).
- **Prefix-based column mapping in `FriendRepositoryImpl`.** Both peers in a `friends` row are loaded from the same `ResultSet` using a `getPeer(resultSet, "peer1")`/`getPeer(resultSet, "peer2")` helper that builds column names dynamically from a prefix string.
- **HikariCP configured statically** via `ConnectionManager` - pool config is shared across all repository instances via a single static `HikariConfig`, with a secondary constructor accepting a `HikariDataSource` for test injection.

## Quick Start

```bash
# requires PostgreSQL running on localhost:5433, database: postgres
# credentials in src/main/resources/application.properties

psql -U postgres -d postgres -f scriptsSql/tables.sql

./mvnw exec:java -Dexec.mainClass=com.fersko.info.Main

# API available at http://localhost:8080/data/{peers,tasks,checks,friends}
# GET /data/peers?id=1
# POST/PUT /data/checks  (JSON body - see DataforTestPostman/)
# DELETE /data/friends?id=3

./mvnw test
```
