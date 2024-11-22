{ pkgs, ... }: {
  # Specify the Nix package channel
  channel = "stable-23.11"; # or "unstable"

  # Define the packages to install
  packages = [
    pkgs.jdk19
  ];

  # Set environment variables (if needed)
  env = {};

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
          command = ["./gradlew" ":bootRun" "--args='--server.port=$PORT'"];
          manager = "web";
        };
      };
    };

    # Workspace lifecycle hooks
    workspace = {
      # Commands to run when the workspace is created
      onCreate = {
        # Example: install dependencies
        # npm-install = 'npm install';
      };
      # Commands to run when the workspace starts
      onStart = {
        # Example: start a background task
        # watch-backend = "npm run watch-backend";
      };
    };
  };
}
