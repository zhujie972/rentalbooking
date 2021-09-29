// created on 9/25 lecture 29
package com.laioffer.staybooking.model;

import javax.persistence.*;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@Entity
@Table(name = "stay_availability")
@JsonDeserialize(builder = StayAvailability.Builder.class)
public class StayAvailability implements Serializable {
    private static final long serialVersionUID = 1L;

    // composite pk : stay + date
    //need a class for it, StayAvailabilityKey
    @EmbeddedId // tell hibernate this is an composite key
    private StayAvailabilityKey id;

    //
    @ManyToOne // 不设置别的东西，默认会生成第三个表 stay - stayAVA 所有对应关系进去
    @MapsId("stay_id")
    private Stay stay;


    private StayAvailabilityState state;
    public StayAvailability(Builder builder) {
        this.id = builder.id;
        this.stay = builder.stay;
        this.state = builder.state;
    }

    public StayAvailability() {

    }

    public StayAvailabilityKey getId() {
        return id;
    }

    public Stay getStay() {
        return stay;
    }

    public StayAvailabilityState getState() {
        return state;
    }

    public static class Builder {
        @JsonProperty("id")
        private StayAvailabilityKey id;

        @JsonProperty("stay")
        private Stay stay;

        @JsonProperty("state")
        private StayAvailabilityState state;

        public Builder setId(StayAvailabilityKey id) {
            this.id = id;
            return this;
        }

        public Builder setStay(Stay stay) {
            this.stay = stay;
            return this;
        }

        public Builder setState(StayAvailabilityState state) {
            this.state = state;
            return this;
        }

        public StayAvailability build() {
            return new StayAvailability(this);
        }
    }



}
