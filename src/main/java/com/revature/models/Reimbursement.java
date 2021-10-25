package com.revature.models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonBackReference;

import com.revature.models.ERSUser.UserRole;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Entity
@Table(name="reimbursements")
public class Reimbursement 
{
	public enum ReimburseStatus { Pending, Approved, Denied};
	public enum ReimburseType { Lodging, Travel, Food, Other};
	
	@Id
	@Column(name="reimburse_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private int id;
	
	@Column(name="reimburse_amount", nullable=false)
	private float amount;
	
	@Column(name="submitted_on")
	private String submittedOn;
	
	@Column(name="resolved_on")
	private String resolvedOn;
	
	private String description;
	
	//private byte[] receiptImage; stretch parameter
	
	@ManyToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name="author_id")
	@JsonBackReference
	private ERSUser author;
	
	@ManyToOne
	@JoinColumn(name="resolver_id")
	private ERSUser resolver;
	
	@Enumerated(EnumType.STRING)
	@Column(name="reimburse_status", nullable=false)
	private ReimburseStatus status;
	
	@Enumerated(EnumType.STRING)
	@Column(name="reimburse_type", nullable=false)
	private ReimburseType type;
		
	public Reimbursement(String amount, String description, String status, String type)
	{
		super();
		this.amount = Float.valueOf(amount);
		this.submittedOn = LocalDateTime.now().toString();
		this.description =  description;
		this.author = new ERSUser("Ervie","test","Ervs","Tubby","yes.email", UserRole.EMPLOYEE, new ArrayList<Reimbursement>());
		this.status = ReimburseStatus.valueOf(status);
		this.type = ReimburseType.valueOf(type);
		this.author.addReimbursement(this);
	}
	
	public Reimbursement()
	{
		super();
	}
	
	public int getID()
	{
		return id;
	}
	
	public void setID(int id)
	{
		this.id = id;
	}
	
	public float getAmount()
	{
		return amount;
	}
	
	public String getSubmitTime()
	{
		return submittedOn;
	}
	
	public String getResolveTime()
	{
		return resolvedOn;
	}
	
	public void setResolveTime(LocalDateTime dateTime)
	{
		this.resolvedOn = dateTime.toString();
	}
	
	public ERSUser getAuthor()
	{
		return author;
	}
	
	public ERSUser getResolver()
	{
		return resolver;
	}
	
	public void setResolver(ERSUser id)
	{
		this.resolver = id;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public ReimburseStatus getStatus()
	{
		return status;
	}
	
	public void setStatus(ReimburseStatus status)
	{
		this.status = status;
	}
	
	public ReimburseType getType()
	{
		return this.type;
	}
	
	public void setType(ReimburseType type)
	{
		this.type = type;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(amount);
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + id;
		result = prime * result + ((resolvedOn == null) ? 0 : resolvedOn.hashCode());
		result = prime * result + ((resolver == null) ? 0 : resolver.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((submittedOn == null) ? 0 : submittedOn.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Reimbursement other = (Reimbursement) obj;
		if (Float.floatToIntBits(amount) != Float.floatToIntBits(other.amount))
			return false;
		if (author == null) {
			if (other.author != null)
				return false;
		} else if (!author.equals(other.author))
			return false;
		if (id != other.id)
			return false;
		if (status != other.status)
			return false;
		if (submittedOn == null) {
			if (other.submittedOn != null)
				return false;
		} else if (!submittedOn.equals(other.submittedOn))
			return false;
		if (type != other.type)
			return false;
		return true;
	}
	
}
