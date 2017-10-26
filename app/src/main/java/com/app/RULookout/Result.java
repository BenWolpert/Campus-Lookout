package com.app.RULookout; /**
 * Created by TRoc9 on 8/10/2017.
 */

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

    public class Result {

        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("data")
        @Expose
        private List<CrimeData> data = null;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public List<CrimeData> getData() {
            return data;
        }

        public void setData(List<CrimeData> data) {
            this.data = data;
        }

    }
