package com.model.DBModel;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.IndexOptions;
import org.mongodb.morphia.annotations.Indexes;

import com.model.Election;
import com.model.Electors;
import com.model.Voters;
import com.model.constituency.Constituency;

import lombok.Data;

@Entity("ConstituencyWiseData")
@Indexes(@Index(fields = { @Field("election"), @Field("constituency") }, options = @IndexOptions(unique = true)))
@Data
public class ConstituencyWiseData {
	@Id private ObjectId id;
	@Embedded
	private final Election election;
	@Embedded
	private final Constituency constituency;
	@Embedded
	private final Electors electors;
	@Embedded
	private final Voters voters;
	private final double turnout;
}
