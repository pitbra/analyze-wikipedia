package de.unileipzig.analyzewikipedia.dumpreader.runner;

import org.junit.runner.notification.RunNotifier;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

/**
 * @author Danilo Morgado
 */
public class ControllerRunner extends BlockJUnit4ClassRunner {
    
    // !!!IMPORTANT TRICKER!!! activate only if you want to test the Controller_Test, because it will reset the neo4j database
    private static final Boolean CLEAN_DB = false;
    
    public ControllerRunner(Class clas) throws InitializationError {
        super(clas);
    }
    
    @Override
    public void runChild(FrameworkMethod method, RunNotifier notifier) {
        
        if (CLEAN_DB) {
            super.runChild(method, notifier);
        } else {
            System.out.format("Testmethod %s was not running to prevent neo4jDB cleaning.%n", method.getMethod());
            notifier.fireTestIgnored(describeChild(method));
        }
    }
    
}
