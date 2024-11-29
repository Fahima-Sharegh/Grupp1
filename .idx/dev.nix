{ pkgs, ... }: {
  # Specify the Nix package channel
  channel = "stable-23.11"; # Use the appropriate NixOS channel

  # Define the packages to install
  packages = [
    pkgs.jdk19       # Matches the Java version in pom.xml
    pkgs.maven       # Required to build the project using Maven
    pkgs.git         # Useful for version control if needed
    pkgs.chromedriver # Install ChromeDriver
    pkgs.google-chrome # Install Google Chrome
  ];

  # Set environment variables
  env = {
    # MAVEN_OPTS can be set here if additional memory or flags are needed
    MAVEN_OPTS = "-Xmx1024m";
  };

  # IDE extensions
  idx = {
    # List of IDE extensions to install
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

    # Enable and configure previews
    previews = {
      enable = true;
      previews = {
        web = {
          # Command to run your application
          command = ["mvn" "spring-boot:run" "--args='--server.port=$PORT'"];
          manager = "web";
        };
      };
    };

    # Workspace lifecycle hooks
    workspace = {
      # Commands to run when the workspace is created
      onCreate = {
        # Example: install dependencies
        maven-deps = "mvn clean install -DskipTests";
      };
      # Commands to run when the workspace starts
      onStart = {
        # Example: start the application
        start-backend = "mvn spring-boot:run";
      };
    };
  };
}
