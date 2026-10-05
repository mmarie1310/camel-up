package com.oasys.server.communication.packets;

import com.google.gson.JsonElement;

import java.util.Objects;

/**
 * This describes an answer message from server to client or vice versa. It gives an answer
 * to the last request, but only if last Packet doesn't have an immediate response packet.
 *
 */
public class SuccessFeedback extends Packet {
    private boolean success;
    private JsonElement request;
    private String error;

    /**
     * Initializes an object of SuccessFeedback.
     *
     * @param success Informs if requset was successful or not.
     * @param error   If the request was not successfull, this parameter shows what exactly went wrong.
     */
    public SuccessFeedback(boolean success, JsonElement request, String error) {
        this.success = success;
        this.request = request;
        this.error = error;
    }

    /**
     * Gets success.
     * @return success.
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Sets success.
     * @param success Informs if requset was successful or not.
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * Gets request.
     * @return request.
     */
    public JsonElement getRequest() {
        return request;
    }

    /**
     * Sets request.
     * @param request Shows name of packet of request.
     */
    public void setRequest(JsonElement request) {
        this.request = request;
    }

    /**
     * Gets error.
     * @return error.
     */
    public String getError() {
        return error;
    }

    /**
     * Sets error.
     * @param error If the request was not successfull, this parameter shows what exactly went wrong.
     */
    public void setError(String error) {
        this.error = error;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SuccessFeedback that = (SuccessFeedback) o;
        return success == that.success && Objects.equals(request, that.request) && Objects.equals(error, that.error);
    }

    @Override
    public int hashCode() {
        return Objects.hash(success, request, error);
    }
}
