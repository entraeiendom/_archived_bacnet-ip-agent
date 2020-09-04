package no.entra.bacnet.agent.commands.cov;

/*
When the parameters "lifetime" and "Issue Confirmed Notifications" are missing the server will understand this request
as cancel subscription.
 */
public class CancelSubscribeCovCommand {

    private final Integer lifetime = null;
    private Boolean confirmedNotifications = null;
    //FIXME
}
