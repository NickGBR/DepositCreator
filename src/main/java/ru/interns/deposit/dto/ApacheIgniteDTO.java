package ru.interns.deposit.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import org.apache.ignite.binary.BinaryObject;
import ru.interns.deposit.enums.Services;
import ru.interns.deposit.external.enums.Status;
import ru.interns.deposit.service.enums.Errors;

import javax.websocket.Decoder;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Builder
@Setter
@Getter
public class ApacheIgniteDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID uuid;

    private Map<Services, Status> serviceStatus;

    private List<Errors> errors;

    @Override
    public String toString() {
        return "ApacheIgniteDTO{" +
                "uuid=" + uuid +
                ", serviceStatus=" + serviceStatus +
                ", errors=" + errors +
                '}';
    }
}
