package com.model.DBModel;

import java.util.ArrayList;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.IndexOptions;

import com.model.Election;
import com.model.constituency.Constituency;
import com.model.result.Result;

@Entity("ConstituencyWiseResults")
@Indexes(@Index(fields = { @Field("election"), @Field("constituency"), @Field("result") }, options = @IndexOptions(unique = true)))
@Data
@RequiredArgsConstructor
public class ConstituencyWiseResult{
	
	@Id private ObjectId id;
	@Embedded
	private final Election election;
	@Embedded
	private final Constituency constituency;
	@Embedded
	private final ArrayList<Result> result;

}
