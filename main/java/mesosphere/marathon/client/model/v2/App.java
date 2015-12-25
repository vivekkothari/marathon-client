package mesosphere.marathon.client.model.v2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import mesosphere.marathon.client.utils.ModelUtils;

public class App {
	private String id;
	private String cmd;
	private Integer instances;
	private Double cpus;
	private Double mem;
	private Map<String, String> labels;
	private Collection<String> uris;
	private List<List<String>> constraints;
	private Container container;
	private Map<String, String> env;
	private String executor;
	private List<Integer> ports;
	private Collection<Task> tasks;
	private Integer tasksStaged;
	private Integer tasksRunning;
	private List<HealthCheck> healthChecks;
	private UpgradeStrategy upgradeStrategy;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public Integer getInstances() {
		return instances;
	}

	public void setInstances(Integer instances) {
		this.instances = instances;
	}

	public Double getCpus() {
		return cpus;
	}

	public void setCpus(Double cpus) {
		this.cpus = cpus;
	}

	public Double getMem() {
		return mem;
	}

	public void setMem(Double mem) {
		this.mem = mem;
	}

	public void setLabels(Map<String, String> labels) { this.labels = labels; }

	public Map<String, String> getLabels() { return labels; }

	public Collection<String> getUris() {
		return uris;
	}

	public void setUris(Collection<String> uris) {
		this.uris = uris;
	}

	public List<List<String>> getConstraints() {
		return constraints;
	}

	public void setConstraints(List<List<String>> constraints) {
		this.constraints = constraints;
	}

	public void addConstraint(String attribute, String operator, String value) {
		if (this.constraints == null) {
			this.constraints = new ArrayList<List<String>>();
		}
		List<String> constraint = new ArrayList<String>(3);
		constraint.add(attribute == null ? "" : attribute);
		constraint.add(operator == null ? "" : operator);
		constraint.add(value == null ? "" : value);
		this.constraints.add(constraint);
	}

	public Container getContainer() {
		return container;
	}

	public void setContainer(Container container) {
		this.container = container;
	}

	public Map<String, String> getEnv() {
		return env;
	}

	public void setEnv(Map<String, String> env) {
		this.env = env;
	}

	public String getExecutor() {
		return executor;
	}

	public void setExecutor(String executor) {
		this.executor = executor;
	}

	public List<Integer> getPorts() {
		return ports;
	}

	public void setPorts(List<Integer> ports) {
		this.ports = ports;
	}

	public void addUri(String uri) {
		if (this.uris == null) {
			this.uris = new ArrayList<String>();
		}
		this.uris.add(uri);
	}

	public void addPort(int port) {
		if (this.ports == null) {
			this.ports = new ArrayList<Integer>();
		}
		this.ports.add(port);
	}

	public Collection<Task> getTasks() {
		return tasks;
	}

	public void setTasks(Collection<Task> tasks) {
		this.tasks = tasks;
	}

	public Integer getTasksStaged() {
		return tasksStaged;
	}

	public void setTasksStaged(Integer tasksStaged) {
		this.tasksStaged = tasksStaged;
	}

	public Integer getTasksRunning() {
		return tasksRunning;
	}

	public void setTasksRunning(Integer tasksRunning) {
		this.tasksRunning = tasksRunning;
	}

	public List<HealthCheck> getHealthChecks() {
		return healthChecks;
	}

	public void setHealthChecks(List<HealthCheck> healthChecks) {
		this.healthChecks = healthChecks;
	}

	public UpgradeStrategy getUpgradeStrategy() { return upgradeStrategy; }

	public void setUpgradeStrategy(UpgradeStrategy upgradeStrategy) { this.upgradeStrategy = upgradeStrategy;}

	@Override
	public String toString() {
		return ModelUtils.toString(this);
	}

	public static DockerAppBuilder dockerAppBuilder(String appId, String dockerImage, Double cpus, Double mem, Integer instances){
		return new DockerAppBuilder(appId, dockerImage, cpus, mem, instances);
	}

	public static class DockerAppBuilder {

		private Collection<Port> portMappings = new ArrayList<Port>();
		private List<Parameter> parameters = new ArrayList<Parameter>();
		private String network = "BRIDGE";
		private String appId = null;
		private Integer instances = 1;
		private String dockerImage = null;
		private Double cpus = null;
		private Double mem = null;
		private boolean forcePullImage = false;
		private boolean privileged = false;
		private Collection<Volume> volumes = new ArrayList<Volume>();

		public  DockerAppBuilder(String appId, String dockerImage, Double cpus, Double mem, Integer instances) {
			this.appId = appId;
			this.dockerImage = dockerImage;
			this.cpus = cpus;
			this.mem = mem;
			this.instances = instances;
		}

		public DockerAppBuilder instances(Integer instances) {
			this.instances = instances;
			return this;
		}

		public DockerAppBuilder paramter(String key, String value){
			Parameter parameter = new Parameter(key, value);
			parameters.add(parameter);
			return this;
		}

		public DockerAppBuilder volume(String containerPath, String hostPath, String mode){
			Volume volume = new Volume();
			volume.setContainerPath(containerPath);
			volume.setHostPath(hostPath);
			volume.setMode(mode);
			volumes.add(volume);
			return this;
		}

		public DockerAppBuilder volume(String containerPath, String hostPath){
			volume(containerPath, hostPath, null);
			return this;
		}

		public DockerAppBuilder portMappings(Integer containerPort, Integer hostPort) {
			Port port = buildPort(containerPort, hostPort);
			portMappings.add(port);
			return this;
		}

		public DockerAppBuilder portMappings(Integer containerPort) {
			portMappings(containerPort, null);
			return this;
		}

		private Port buildPort(Integer containerPort, Integer hostPort) {
			Port port = new Port(containerPort);
			port.setHostPort(hostPort);
			return port;
		}

		public DockerAppBuilder network(String network) {
			this.network = network;
			return this;
		}

		public DockerAppBuilder forcePullImage(boolean forcePullImage){
			this.forcePullImage = forcePullImage;
			return this;
		}

		public DockerAppBuilder privileged(boolean privileged){
			this.privileged = privileged;
			return this;
		}

		public App build() {
			Docker docker = new Docker();
			docker.setImage(dockerImage);
			docker.setForcePullImage(forcePullImage);
			docker.setPrivileged(privileged);
			docker.setNetwork(network);

			if (!portMappings.isEmpty()) {
				docker.setPortMappings(portMappings);
			}

			if(!parameters.isEmpty()){
				docker.setParameters(parameters);
			}

			Container container = new Container();
			container.setType("DOCKER");
			container.setDocker(docker);

			if(!volumes.isEmpty()){
				container.setVolumes(volumes);
			}

			App app = new App();
			app.setId(appId);
			app.setInstances(instances);
			app.setCpus(cpus);
			app.setMem(mem);
			app.setContainer(container);

			return app;
		}
	}
}
