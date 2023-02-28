package br.com.unipix.api.enumaration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusHistoricoEnum {
	
	VIGENTE(0, "Vigente"),
	VENCIDO(1, "Vencido"),
	AGENDADO(2, "Agendado"),
	CANCELADO(3, "Cancelado");
	
	private Integer id;
	private String name;
	
	public static StatusHistoricoEnum getById(Integer id) {
	    for(StatusHistoricoEnum e : values()) {
	        if(e.id.equals(id)) return e;
	    }
	    throw new IllegalArgumentException(String.format("Não existe uma constante para o valor %d no ENUM %s",  id, StatusHistoricoEnum.class.getName()));
	}

}
