<h2>Running the Application with Docker Compose</h2>
<ul>
    <li><strong>Requirements:</strong> Make sure you have Docker and Docker Compose installed on your machine.</li> 
     <li><strong>Clone Repository:</strong> Clone the repository containing your Java and Angular application.</li>
    <li><strong>Navigate to the Project Directory:</strong> Open a terminal and navigate to the root directory of your project.</li> 
    <li><strong>Build the Docker Images:</strong> Run the following command to build the Docker images defined in the <code>docker-compose.yaml</code> file:
        <pre><code>docker-compose build</code></pre>
    </li>   
    <li><strong>Start the Containers:</strong> Run the following command to start the Docker containers:
        <pre><code>docker-compose up</code></pre>
    </li>
    <li><strong>Access the Application:</strong>
        <ul>
            <li>The Java backend will be available at <a href="http://localhost:8080">http://localhost:8080</a>.</li>
            <li>The Angular frontend will be available at <a href="http://localhost:4200">http://localhost:4200</a>.</li>
            <li>PHPMyAdmin (if needed) will be available at <a href="http://localhost:8081">http://localhost:8081</a>.</li>
        </ul>
    </li>
    <li><strong>Stopping the Application:</strong> To stop the application and shut down the Docker containers, press <kbd>Ctrl + C</kbd> in the terminal where you started the containers.</li>
</ul>
<h3>Notes:</h3>
<ul>
    <li>The provided <code>docker-compose.yaml</code> file defines services for the backend, MySQL database, frontend, and PHPMyAdmin.</li>
    <li>The <code>depends_on</code> directive ensures that necessary services are started in the correct order.</li>
    <li>Port mappings are defined to expose services to the host machine.</li>
    <li>Docker volumes are used to persist MySQL data.</li>
    <li>Networks are defined to allow communication between services.</li>
</ul>
<h3>IntelliJ IDEA Integration:</h3>
<p>You can run the application directly from IntelliJ IDEA by opening the project and clicking on the Docker icon in the sidebar, then selecting <code>docker-compose.yaml</code> and clicking on the green play button to start the services.</p>
<h3>Docker Compose Configuration Details:</h3>
<p>In the <code>docker-compose.yaml</code> file:</p>
<ul>
    <li>The <code>backend</code> service is configured to build its Docker image using the following configuration:</li>
    <pre><code>build: ../english-app</code></pre>
    <p>This specifies the directory containing the Dockerfile for building the backend image.</p>
    <li>The <code>frontend</code> service is configured to build its Docker image using the following configuration:</li>
    <pre><code>build:
  context: ../english-app-angular</code></pre>
    <p>This specifies the directory containing the Dockerfile for building the frontend image.</p>
    <li>Ensure that the paths specified in the <code>build</code> and <code>context</code> fields match the actual directory structure of your project.</li>
</ul>
