/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.moov.report.disk;

import org.apache.log4j.Logger;

/**
 *
 * @author esombugma
 */
public class AppLogger {

    private static Logger applogger;

    /**
     * @return the applogger
     */
    public static Logger getApplogger() {
        return applogger;
    }

    /**
     * @param aApplogger the applogger to set
     */
    public static void setApplogger(Logger aApplogger) {
        applogger = aApplogger;
    }
}
