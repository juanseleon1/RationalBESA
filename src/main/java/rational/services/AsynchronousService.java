package rational.services;

import BESA.Adapter.AdapterBESA;
import BESA.Kernel.Agent.Event.DataBESA;
import BESA.Kernel.Social.ServiceProvider.agent.SPService;
import BESA.Kernel.Social.ServiceProvider.agent.SPServiceDataRequest;
import BESA.Kernel.Social.ServiceProvider.agent.StateServiceProvider;

/**
 * An abstract class representing an asynchronous service. This class extends
 * the SPService class.
 */
public abstract class AsynchronousService extends SPService {

    public AsynchronousService(String name, AdapterBESA adapter) {
        super(name, adapter);
    }

    /**
     * This method is not supported for AsynchronousService and throws an
     * UnsupportedOperationException.
     *
     * @param data The service data request.
     * @param adapter The BESA adapter.
     * @return DataBESA Not supported for AsynchronousService.
     * @throws UnsupportedOperationException Always thrown, as this method is
     * not supported.
     */
    @Override
    public DataBESA executeService(SPServiceDataRequest data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Executes the asynchronous service with the given data and adapter.
     *
     * @param data The service data request.
     * @param adapter The BESA adapter.
     * @param subscribeAgents A map of subscribed agents and their corresponding
     * SPInfoGuard instances.
     */
    public abstract void executeAsyncService(SPServiceDataRequest data, StateServiceProvider ssp);


        /**
     * Executes the asynchronous service with the given data and adapter.
     *
     * @param data The service data request.
     * @param adapter The BESA adapter.
     * @param subscribeAgents A map of subscribed agents and their corresponding
     * SPInfoGuard instances.
     */
    public abstract void replyToAsyncService(DataBESA data, StateServiceProvider ssp);

}
