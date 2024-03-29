/*
 *     Copyright (C) 2019  Blaumeise03 - bluegame61@gmail.com
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package de.blaumeise03.utils.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Class for easy-http-requests
 */
public class HttpRequest {
    private int responseCode = -1;
    private String response = null;
    private HttpURLConnection connection = null;
    private String url;
    private Map<String,String> headerParameters;
    private IOException exception = null;

    /**
     * Constructor for HttpRequest.
     *
     * @param url              the target URL
     * @param headerParameters all parameters for the header (e.g. "User-Agent: Java-Client")
     */
    public HttpRequest(String url, Map<String,String> headerParameters){
        this.url = url;
        this.headerParameters = headerParameters;
    }

    /**
     * Method for executing the GET request. If the request fails with an exception it will return false, you can access the exception with {@link HttpRequest#getException()}.
     *
     * @return true if the request is successfully complete.
     */
    public boolean executeGet(){
        responseCode = -1;
        response = null;

        try {
            URL urlObj = new URL(url);

            connection = (HttpURLConnection) urlObj.openConnection();

            connection.setRequestMethod("GET");
            headerParameters.forEach(connection::setRequestProperty);

            responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader inputReader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = inputReader.readLine()) != null) {
                    response.append(inputLine);
                }

                inputReader.close();
                this.response = response.toString();
            }
        } catch (IOException e) {
            //e.printStackTrace();
            exception = e;
            return false;
        }

        return true;
    }

    /**
     * Getter for the response-code (if {@link HttpRequest#executeGet()} is not executed before, the response-code is -1)
     *
     * @return the response-code
     */
    public int getResponseCode() {
        return responseCode;
    }

    /**
     * Getter for the response (if {@link HttpRequest#executeGet()} is not executed before, the response is null)
     *
     * @return the response-message
     */
    public String getResponse() {
        return response;
    }

    /**
     * Getter for the {@link HttpURLConnection}.
     *
     * @return the {@link HttpURLConnection}
     */
    public HttpURLConnection getConnection() {
        return connection;
    }

    /**
     * Getter for the URL
     *
     * @return the URL of the request
     */
    public String getUrl() {
        return url;
    }

    /**
     * Getter for the header-parameters
     *
     * @return the header-parameters
     */
    public Map<String, String> getHeaderParameters() {
        return headerParameters;
    }

    /**
     * Getter for the exception (only if {@link HttpRequest#executeGet()} returns false)
     *
     * @return the {@link IOException, IOException} if {@link HttpRequest#executeGet()} returns false, else it will return null.
     */
    public IOException getException() {
        return exception;
    }
}
