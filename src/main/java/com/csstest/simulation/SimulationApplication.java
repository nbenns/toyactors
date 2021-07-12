package com.csstest.simulation;

import com.csstest.simulation.actors.couriermanager.CMProps;
import com.csstest.simulation.actors.couriermanager.CourierManager;
import com.csstest.simulation.actors.ordersubmitter.OSProps;
import com.csstest.simulation.actors.ordersubmitter.OrderSubmitter;
import com.csstest.simulation.actors.shelfmanager.SHProps;
import com.csstest.simulation.actors.shelfmanager.ShelfHandler;
import com.csstest.simulation.domain.Order;
import com.csstest.simulation.domain.ShelfLayout;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import lombok.val;

import java.net.URL;
import java.util.List;
import java.util.Optional;

public class SimulationApplication {
	private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new Jdk8Module());

	private SimulationApplication() {

	}

	private Optional<List<Order>> readFile(URL file) {
		try {
			val contents =
				objectMapper
					.readValue(file, new TypeReference<List<Order>>() {});

			return Optional.ofNullable(contents);
		}
		catch (Exception ex) {
			System.out.println(ex);
			return Optional.empty();
		}
	}

	public static void main(String[] args) {
		// Delay to give time to connect with VisualVM
		Utils.sleep(5000L);

		val app = new SimulationApplication();

		val file = app.getClass().getClassLoader().getResource("orders.json");

		val fileContents = app.readFile(file);

		fileContents.ifPresentOrElse( orders -> {
			val shelfLayout = new ShelfLayout();

			val os = OrderSubmitter.make();
			val sh = ShelfHandler.make();
			val cm = CourierManager.make();

			val osProps = new OSProps(orders, sh.getCommandQueue(), 2);
			val shProps = new SHProps(shelfLayout, cm.getCommandQueue());
			val cmProps = new CMProps(sh.getCommandQueue());

			val osThread = os.start("OrderSubmitter", osProps);
			val shThread = sh.start("ShelfHandler", shProps);
			val cmThread = cm.start("CourierManager", cmProps);

			while(osThread.isAlive() || shThread.isAlive() || cmThread.isAlive()) {
				Utils.sleep(100L);
			}

			// Wait for Couriers to complete.
			Utils.sleep(10000L);

			System.exit(0);
		},
		() -> System.out.println("Cannot read file"));
	}
}
