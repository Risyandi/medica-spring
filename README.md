# Medica Spring

Medica Spring is a Spring Boot-based application designed to provide a robust backend for medical-related services.

## Features

- Spring Boot framework for rapid development
- Organized project structure
- Gradle build system
- Configuration via `application.yml`
- Ready for integration with databases and other services

## Getting Started

### Prerequisites

- Java 17 or higher
- Gradle

### Build & Run

1. **Build the project:**

   ```bash
   gradle build
   ```

2. **Run the application:**
   ```bash
   gradle bootRun
   ```

### Quick Run Commands

Here are some common Gradle commands you can use while developing and running the application. Each command includes a short note about when to use it.

- Start the application locally (runs the Spring Boot app on the configured port, default 8080):

```bash
gradle bootRun
```

- Build the project (runs tests, suitable for CI or verifying everything):

```bash
gradle build
```

- Build the project but skip tests (faster for local iterations). The `--no-daemon` flag forces a single-use JVM which can be helpful in some CI or troubleshooting scenarios:

```bash
gradle build -x test --no-daemon
```

- Build the project but skip tests while using the Gradle daemon (faster repeated builds locally):

```bash
gradle build -x test
```

- Build the project ensuring the Gradle daemon is disabled (useful to respect JVM settings or in some CI environments):

```bash
gradle build --no-daemon
```

- Clean and assemble the runnable jar (creates the Jar in `build/libs`):

```bash
gradle clean bootJar
```

- Run the generated jar (replace `<your-jar-file>.jar` with the actual filename produced in `build/libs`):

```bash
java -jar build/libs/<your-jar-file>.jar
```

Use the above commands according to your workflow: `bootRun` for quick development runs, `gradle build` for full verification (including tests), and `-x test` or `--no-daemon` when you need faster or environment-specific builds.

### Configuration

Modify `application.yml` to set up your environment variables, database connections, and other settings.

## Contributing

Feel free to fork the repository and submit pull requests. For major changes, please open an issue first to discuss what you would like to change.
