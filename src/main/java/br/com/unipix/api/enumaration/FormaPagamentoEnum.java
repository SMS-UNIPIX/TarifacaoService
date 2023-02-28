package br.com.unipix.api.enumaration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FormaPagamentoEnum {
    POSPAGO(0, "Pós-Pago"),
    PREPAGO(1, "Pré-Pago");

    private final Integer id;
    private final String nome;

    public static FormaPagamentoEnum getById(Integer id) {
        for (FormaPagamentoEnum e : values()) {
            if (e.id.equals(id)) return e;
        }
        throw new IllegalArgumentException(String.format("Não existe uma constante para o valor %d no ENUM %s", id, FormaPagamentoEnum.class.getName()));
    }
}
