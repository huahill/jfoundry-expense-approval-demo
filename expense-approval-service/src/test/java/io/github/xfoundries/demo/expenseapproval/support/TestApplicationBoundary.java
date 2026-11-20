package io.github.xfoundries.demo.expenseapproval.support;

import io.huahill.jfoundry.application.ApplicationService;

@ApplicationService
public class TestApplicationBoundary {

    public void run(Runnable action) {
        action.run();
    }
}
