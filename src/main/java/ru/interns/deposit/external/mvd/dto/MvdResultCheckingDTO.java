package ru.interns.deposit.external.mvd.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.interns.deposit.external.enums.CheckingStatus;
import ru.interns.deposit.external.mvd.enums.MvdErrors;
import java.util.*;

@Builder
@Getter
@Setter
public class MvdResultCheckingDTO {
    private CheckingStatus checkingStatus;
    private List<MvdErrors> mvdErrorsList;
}
