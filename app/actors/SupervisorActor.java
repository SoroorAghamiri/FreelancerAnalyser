package actors;

import akka.actor.*;
import akka.japi.pf.DeciderBuilder;

import java.time.Duration;

class SupervisorActor extends AbstractActor { 

	private static SupervisorStrategy strategy =
			new OneForOneStrategy(
					10,
					Duration.ofMinutes(1),
					DeciderBuilder.match(Exception.class, e -> SupervisorStrategy.resume())
							.build());

	@Override
	public SupervisorStrategy supervisorStrategy() {
		return strategy;
	}

	@Override public Receive createReceive() {
		return receiveBuilder()
				.match(
						Props.class,
						props -> {
							getSender().tell(getContext().actorOf(props), getSelf());
						})
				.build(); 
		} }
