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

public class HttpRequest {
    private int responseCode;
    private String response;
    private HttpURLConnection connection;
    private String url;
    private Map<String,String> headerParameters;

    public HttpRequest(String url, Map<String,String> headerParameters){
        this.url = url;
        this.headerParameters = headerParameters;
    }

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
            return false;
        }

        return true;
    }

}
