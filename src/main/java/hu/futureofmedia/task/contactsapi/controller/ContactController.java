package hu.futureofmedia.task.contactsapi.controller;

import hu.futureofmedia.task.contactsapi.dto.ContactCreateUpdateCommand;
import hu.futureofmedia.task.contactsapi.dto.ContactInfo;
import hu.futureofmedia.task.contactsapi.dto.ContactMinInfo;
import hu.futureofmedia.task.contactsapi.exception.ErrorMessage;
import hu.futureofmedia.task.contactsapi.service.ContactService;
import hu.futureofmedia.task.contactsapi.utility.LoggerHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping(
        path = "/api/v1/contacts",
        produces = MediaType.APPLICATION_JSON_VALUE)
public class ContactController {

    private final ContactService contactService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactController.class);

    private final HttpServletRequest request;

    public ContactController(ContactService contactService, HttpServletRequest request) {
        this.contactService = contactService;
        this.request = request;
    }


    @Operation(
            summary = "Kapcsolattartó létrehozása")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Kapcsolattartó sikeresen létrehozva",
                    content = @Content(schema = @Schema(implementation = ContactInfo.class))),
            @ApiResponse(responseCode = "400", description = "Bemeneti adatok nem megfelelőek",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ErrorMessage.class)))),
            @ApiResponse(responseCode = "404", description = "Nem található cég a megadott ID-vel",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ErrorMessage.class)))) })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ContactInfo createContact(
            @Valid @RequestBody ContactCreateUpdateCommand command) {
        LoggerHelper.requestLog(LOGGER, request, command);
        return contactService.createContact(command);
    }

    @Operation(
            summary = "Kapcsolattartókat tartalmaző oldal lekérdezése",
            parameters = @Parameter(in = ParameterIn.QUERY, name = "page", description = "Lekérni kívánt oldalszám (0..N)",
                    content = @Content(schema = @Schema(defaultValue = "0", example = "0"))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kapcsolattartók sikeresen lekérdezve",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ContactInfo.class)))),
            @ApiResponse(responseCode = "400", description = "Megadott oldalszám negatív",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ErrorMessage.class)))) })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<ContactMinInfo> getAllContacts(
            @RequestParam(value = "page", defaultValue = "0") Integer page) {
        LoggerHelper.requestLog(LOGGER, request);
        return contactService.getAllContacts(page);
    }

    @Operation(
            summary = "Kapcsolattartó részletes adatainak lekérdezése ID alapján",
            parameters = @Parameter(in = ParameterIn.PATH, name = "id", example = "1", description = "Lekérdezni kívánt kapcsolattartó ID-je"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kapcsolattartó sikeresen lekérdezve",
                    content = @Content(schema = @Schema(implementation = ContactInfo.class))),
            @ApiResponse(responseCode = "404", description = "Nem található kapcsolattartó a megadott ID-vel",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ErrorMessage.class)))) })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ContactInfo getContact(
            @PathVariable("id") Long id) {
        LoggerHelper.requestLog(LOGGER, request);
        return contactService.getContact(id);
    }

    @Operation(
            summary = "Kapcsolattartó módosítása ID alapján",
            parameters = @Parameter(in = ParameterIn.PATH, name = "id", example = "1", description = "Módosítani kívánt kapcsolattartó ID-je"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kapcsolattartó sikeresen módosítva",
                    content = @Content(schema = @Schema(implementation = ContactInfo.class))),
            @ApiResponse(responseCode = "400", description = "Bemeneti adatok nem megfelelőek",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ErrorMessage.class)))),
            @ApiResponse(responseCode = "404", description = "Nem található kapcsolattartó vagy cég a megadott ID-vel",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ErrorMessage.class)))) })
    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ContactInfo updateContact(
            @PathVariable("id") Long id,
            @Valid @RequestBody ContactCreateUpdateCommand command) {
        LoggerHelper.requestLog(LOGGER, request, command);
        return contactService.updateContact(id, command);
    }

    @Operation(
            summary = "Kapcsolattartó törlése ID alapján",
            parameters = @Parameter(in = ParameterIn.PATH, name = "id", example = "1", description = "Törölni kívánt kapcsolattartó ID-je"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kapcsolattartó sikeresen törölve", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Nem található kapcsolattartó a megadott ID-vel", content = @Content())})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteContactBy(
            @PathVariable("id") Long id) {
        LoggerHelper.requestLog(LOGGER, request);
        contactService.deleteContact(id);
    }
}
