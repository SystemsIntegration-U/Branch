package systems.integration.branch.Interface;

import java.util.concurrent.CompletableFuture;

public interface IRequestHandler<TCommand, TResult> {
    CompletableFuture<TResult> handle(TCommand command);
}
