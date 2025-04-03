package com.oleksandr.junit;

import com.oleksandr.junit.service.UserServiceTest;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TagFilter;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;

import java.io.PrintWriter;

public class TestLauncher {

    public static void main(String[] args) {
        var launcher = LauncherFactory.create();

        // launcher.registerLauncherDiscoveryListeners();
        // launcher.registerTestExecutionListeners();

        var summaryGeneratingListener = new SummaryGeneratingListener(); // to see statistics
        //launcher.registerTestExecutionListeners(summaryGeneratingListener);

        LauncherDiscoveryRequest discoveryRequest =
                LauncherDiscoveryRequestBuilder
                        .request()
                        .selectors(DiscoverySelectors.selectClass(UserServiceTest.class))
                        .selectors(DiscoverySelectors.selectPackage("com.oleksandr.junit"))
                        .filters(
                                TagFilter.includeTags("login")
//                                TagFilter.excludeTags("login")
                        )
                        .build();
        launcher.execute(discoveryRequest,summaryGeneratingListener);

        try(var writer = new PrintWriter(System.out);) {
            summaryGeneratingListener.getSummary().printTo(writer);
        }
    }

}

