{ pkgs, ... }: {
  # Specify the packages to install
  packages = [
    pkgs.jdk19          # Java JDK 19 for your project
    pkgs.maven          # Maven for building the project
    pkgs.git            # Git for version control
    pkgs.chromedriver   # ChromeDriver for Selenium
    pkgs.chromium       # Chromium (alternative to Google Chrome)
  ];

  # Environment variables
  env = {
    # Set memory options for Maven
    MAVEN_OPTS = "-Xmx1024m";
  };

  # IDE extensions
  idx = {
    # List of IDE extensions for Java and Spring Boot development
    extensions = [
      "redhat.java"
      "vscjava.vscode-java-debug"
      "vscjava.vscode-java-dependency"
      "vscjava.vscode-java-pack"
      "vscjava.vscode-java-test"
      "vscjava.vscode-maven"
      "Pivotal.vscode-boot-dev-pack"
      "vmware.vscode-spring-boot"
      "vscjava.vscode-spring-boot-dashboard"
      "vscjava.vscode-spring-initializr"
    ];

    # Preview configuration for running the application
    previews = {
      enable = true;
      previews = {
        web = {
          command = ["mvn" "spring-boot:run" "--args='--server.port=$PORT'"];
          manager = "web";
        };
      };
    };

    # Lifecycle hooks for workspace
    workspace = {
      onCreate = {
        # Install Maven dependencies when the workspace is created
        maven-deps = "mvn clean install -DskipTests";
      };
      onStart = {
        # Start the backend application
        start-backend = "mvn spring-boot:run";
      };
    };
  };
}
