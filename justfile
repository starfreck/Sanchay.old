# Sanchay Project Automation

# Default task: list all available recipes
default:
    @just --list

# --- Common Tasks ---

# Clean all build artifacts
clean:
    @echo "🧹 Cleaning all build artifacts..."
    ./gradlew clean
    @echo "✅ Clean complete!"

# Run all tests across the project
test-all:
    @echo "🧪 Running all tests..."
    ./gradlew test
    @echo "✅ All tests complete!"

# --- Server (Spring Boot) ---

# Run the Spring Boot server
server-run:
    @echo "🚀 Starting Spring Boot server..."
    ./gradlew :server:bootRun

# Run server tests
server-test:
    @echo "🧪 Running server tests..."
    ./gradlew :server:test

# Build server JAR
server-build:
    @echo "📦 Building server JAR..."
    ./gradlew :server:bootJar
    @echo "✅ Server build complete!"

# Start PostgreSQL via Docker Compose
db-up:
    @echo "🐘 Starting PostgreSQL..."
    docker compose -f server/compose.yaml up -d

# Stop PostgreSQL
db-down:
    @echo "🐘 Stopping PostgreSQL..."
    docker compose -f server/compose.yaml down

# --- Android ---

# Build Android debug APK
android-build:
    @echo "🤖 Building Android debug APK..."
    ./gradlew :composeApp:assembleDebug
    @echo "✅ Android build complete!"

# Install and run on connected Android device
android-run:
    @echo "🤖 Running Android app..."
    ./gradlew :composeApp:installDebug

# --- Desktop (JVM) ---

# Run the desktop application
desktop-run:
    @echo "🖥️ Starting Desktop application..."
    ./gradlew :composeApp:run

# Build desktop distribution
desktop-dist:
    @echo "📦 Building Desktop distribution..."
    ./gradlew :composeApp:package
    @echo "✅ Desktop distribution complete!"

# --- Web ---

# Run Web application (Wasm target)
web-wasm-run:
    @echo "🌐 Starting Web application (Wasm)..."
    ./gradlew :composeApp:wasmJsBrowserDevelopmentRun

# Run Web application (JS target)
web-js-run:
    @echo "🌐 Starting Web application (JS)..."
    ./gradlew :composeApp:jsBrowserDevelopmentRun

# Build Web distribution (Wasm)
web-wasm-build:
    @echo "📦 Building Web distribution (Wasm)..."
    ./gradlew :composeApp:wasmJsBrowserDistribution
    @echo "✅ Web (Wasm) build complete!"

# --- iOS ---

# Note: iOS runs are typically handled via Xcode or specialized Gradle tasks
# Build iOS framework (for use in Xcode)
ios-build:
    @echo "🍎 Building iOS framework..."
    ./gradlew :composeApp:embedAndSignAppleFrameworkForXcode
    @echo "✅ iOS build complete!"
