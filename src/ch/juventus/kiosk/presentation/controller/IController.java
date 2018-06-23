/**
 * interface for java fc view controller
 *
 * @author Miguel Iglesias
 */
package ch.juventus.kiosk.presentation.controller;

import javafx.scene.control.Alert;

public interface IController {

    /**
     * Render master panel
     */
    void renderMasterPanel();

    /**
     * Clears master panel
     */
    void clearMasterPanel();

    /**
     * Renders view panel
     */
    void renderViewPanel();

    /**
     * Clears view panel
     */
    void clearViewPanel();

    /**
     * Renders detail panel
     */
    void renderDetailsPanel();

    /**
     * Clears details panel
     */
    void clearDetailsPanel();

    /**
     * Generates java fx alert
     *
     * @param title alert title
     * @param text alert text
     * @param alertType alert severity
     */
    void generateAlert(String title, String text, Alert.AlertType alertType);
}
