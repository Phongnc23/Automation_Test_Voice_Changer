   # Voice Effects Update Plan (REVISED — DOM mới)

> ⚠️ Plan này đã được **rewrite** sau khi user update lại DOM. Khác biệt lớn vs version cũ:
> 1. Effects list = **34 effects** (cũ là 23) — thêm 12 mới, bỏ 1 cũ
> 2. **Volume block ĐỘNG** — vị trí thay đổi theo effect đang được chọn (mặc định Normal → Volume nằm sau row Normal/Man/Woman; nếu chọn Devil → Volume nằm sau row chứa Devil)

---

## DOM Analysis

### Files đã đọc (4 file, structure giống nhau)

- `docs/dom/Record/VoiceEffects.txt`
- `docs/dom/TextToSpeech/VoiceEffects.txt`
- `docs/dom/MyAudio/VoiceEffects.txt`
- `docs/dom/ImportAudio/VoiceEffects.txt`

Tất cả 4 file đều có structure giống nhau, chỉ khác `tvAudioName` (ten file audio):
- Record: `record_*.m4a`
- TTS: `tts_part_*.wav`
- MyAudio: `src_*_record_*.m4a`
- ImportAudio: `import_*_Âm Thầm Bên Em*.mp3` (file đã chọn)

### Layout tổng quan (DOM MỚI)

```
layoutHeader
  ├── btnClose (X)
  └── tvTitle = "Voice Effects"

layoutAudio
  ├── image_wave + tvAudioName
  └── btnPlayPause + seekBar + tvTime

rvEffects (RecyclerView, scrollable=true) — TỔNG 34 effects + 1 Volume block động
  ├── Row 0: [Normal] | Man | Woman          (default selected = Normal)
  ├── ★ VOLUME BLOCK (vị trí ĐỘNG theo selected effect)
  │     ├── tvVolumeLabel = "Volume"
  │     ├── btnResetVolume
  │     ├── seekVolume (text="100.0", range 0..200)
  │     ├── (TextView "0")
  │     ├── tvVolumeValue (text="100")
  │     └── (TextView "200")
  ├── Row 1: Child | Baby | Tenor
  ├── Row 2: Helium | Parody | Whisper
  ├── Row 3: Cyborg | Devil | Ghost
  ├── Row 4: Penguin | Monster | Death
  ├── Row 5: Zombie | Drunk | Alien
  ├── Row 6: Bee | Robot | Fast
  ├── Row 7: Slow | Bass | Echo
  ├── Row 8: Tremolo | Bathroom | Cave
  ├── Row 9: Hall | Stadium | Tunnel
  └── Row 10: Underwater | Telephone | Megaphone
  └── Row 11: Old Radio (1 mình)

layoutBottom
  ├── btnSend = "Send Voice Message"
  └── btnSave = "Save"
```

### ✅ Danh sách 34 effects (DOM mới)

| # | Effect | Trong list cũ? |
|---|--------|----------------|
| 1 | Normal | ✓ |
| 2 | Man | ✓ |
| 3 | Woman | ✓ |
| 4 | Child | ✓ |
| 5 | Baby | ✓ |
| 6 | Tenor | ✓ |
| 7 | Helium | ✓ |
| 8 | Parody | ✓ |
| 9 | **Whisper** | ✗ MỚI |
| 10 | **Cyborg** | ✗ MỚI |
| 11 | **Devil** | ✗ MỚI |
| 12 | **Ghost** | ✗ MỚI |
| 13 | Penguin | ✓ |
| 14 | Monster | ✓ |
| 15 | Death | ✓ |
| 16 | Zombie | ✓ |
| 17 | Drunk | ✓ |
| 18 | Alien | ✓ |
| 19 | Bee | ✓ |
| 20 | Robot | ✓ |
| 21 | Fast | ✓ |
| 22 | Slow | ✓ |
| 23 | Bass | ✓ |
| 24 | Echo | ✓ |
| 25 | **Tremolo** | ✗ MỚI |
| 26 | **Bathroom** | ✗ MỚI |
| 27 | **Cave** | ✗ MỚI |
| 28 | **Hall** | ✗ MỚI |
| 29 | **Stadium** | ✗ MỚI |
| 30 | **Tunnel** | ✗ MỚI |
| 31 | Underwater | ✓ |
| 32 | Telephone | ✓ |
| 33 | **Megaphone** | ✗ MỚI |
| 34 | **Old Radio** | ✗ MỚI |

### Effects BIẾN MẤT khỏi DOM (so với ALL_EFFECTS cũ)

- ❌ **Fade** — không còn trong DOM mới

### Element MỚI (Volume control)

| Resource ID | Type | Default | Phạm vi |
|-------------|------|---------|---------|
| `tvVolumeLabel` | TextView | "Volume" | (label) |
| `btnResetVolume` | ImageView (clickable) | (icon) | reset về 100 |
| `seekVolume` | SeekBar | text="100.0" | 0..200 |
| `tvVolumeValue` | TextView | "100" | giá trị hiện tại |

### Element GIỮ NGUYÊN (resource-id, locator không đổi)

- `layoutHeader`, `btnClose`, `tvTitle`
- `layoutAudio`, `image_wave`, `tvAudioName`
- `btnPlayPause`, `seekBar`, `tvTime`
- `rvEffects`, `tv_effect_name`, `img`
- `layoutBottom`, `btnSend`, `btnSave`

### 🔑 Behavior MỚI quan trọng

> **Volume block động theo effect được chọn**: khi click 1 effect bất kỳ → Volume block sẽ render NGAY DƯỚI row của effect đó. Ví dụ:
> - Mặc định chọn Normal → Volume block ở giữa row 0 và row 1
> - Click Devil → Volume block move xuống dưới row 3 (Cyborg/Devil/Ghost)
> - Click Old Radio → Volume block move xuống cuối (sau row 11)
>
> **Hệ quả cho automation**:
> - Sau mỗi `clickEffect()`, layout grid sẽ refresh → bounds của effect items thay đổi
> - `smartSwipeToFind` dùng resource-id + text → vẫn work, không phụ thuộc bounds
> - Tuy nhiên khi smart-swipe đi qua effect ĐÃ chọn, có thể đụng phải `seekVolume` thumb → cần né (lệch x sang trái/phải)

---

## Page Object hiện tại

### Locator đã có (vẫn tương thích DOM mới)

`HEADER_LAYOUT`, `CLOSE_BUTTON`, `TITLE`, `AUDIO_LAYOUT`, `AUDIO_NAME`, `IMAGE_WAVE`, `PLAY_PAUSE_BUTTON`, `SEEK_BAR`, `TIME_TEXT`, `EFFECTS_RECYCLER`, `EFFECT_NAME_ITEMS`, `BOTTOM_LAYOUT`, `SEND_BUTTON`, `SAVE_BUTTON`

### Method đã có (vẫn tương thích DOM mới)

- UI: `isDisplayed`, `isHeaderDisplayed`, `isCloseButtonDisplayed`, `getTitle`, `isTitleDisplayed`
- Audio: `isAudioPlayerDisplayed`, `getAudioFileName`, `isImageWaveDisplayed`, `getTimeText`, `clickPlayPause`, `isAudioPlaying`, etc.
- Effects: `isEffectsRecyclerDisplayed`, `getEffectLocator`, `getVisibleEffectNames`, `clickEffect`, `isEffectDisplayed`, `countVisibleEffects`, `smartSwipeToFind`
- Actions: `clickClose`, `clickSave`, `clickSendVoiceMessage`
- Discard dialog: `getDiscardDialog`, `isDiscardDialogDisplayed`, `clickDiscardCancel`, `clickDiscardConfirm`

### Constant `ALL_EFFECTS` cần UPDATE

Hiện tại (23 effects, có "Fade"):
```java
"Normal", "Man", "Woman", "Child", "Penguin", "Monster",
"Fast", "Slow", "Alien", "Zombie", "Drunk", "Helium",
"Death", "Robot", "Baby", "Echo", "Underwater", "Telephone",
"Parody", "Bass", "Tenor", "Bee", "Fade"
```

Đề xuất mới (34 effects, KHÔNG có "Fade"):
```java
// Group 1: Voice tone (gioi tinh/tuoi)
"Normal", "Man", "Woman", "Child", "Baby", "Tenor",
// Group 2: Pitch/character
"Helium", "Parody", "Whisper", "Cyborg", "Devil", "Ghost",
"Penguin", "Monster", "Death", "Zombie", "Drunk", "Alien",
"Bee", "Robot",
// Group 3: Speed
"Fast", "Slow",
// Group 4: Frequency / EQ
"Bass", "Echo", "Tremolo",
// Group 5: Room / environment
"Bathroom", "Cave", "Hall", "Stadium", "Tunnel",
"Underwater", "Telephone", "Megaphone", "Old Radio"
```

### Cần THÊM (Volume control)

```java
// Locators
private static final By VOLUME_LABEL       = By.id(".../tvVolumeLabel");
private static final By VOLUME_RESET_BTN   = By.id(".../btnResetVolume");
private static final By VOLUME_SEEKBAR     = By.id(".../seekVolume");
private static final By VOLUME_VALUE_TEXT  = By.id(".../tvVolumeValue");

// Methods
public boolean isVolumeSectionDisplayed()
public boolean isVolumeLabelDisplayed()
public boolean isVolumeResetButtonDisplayed()
public boolean isVolumeSeekBarDisplayed()
public boolean isVolumeValueTextDisplayed()
public String getVolumeValue()             // text "100"
public String getVolumeSeekBarText()       // text "100.0"
public void clickResetVolume()
public void dragVolumeTo(double percent)   // 0.0..1.0
public void dragVolumeLeft()               // decrease
public void dragVolumeRight()              // increase
```

### Cần SỬA

- **`ALL_EFFECTS`**: Update list (đã giải thích ở trên)
- **`clickEffect()`** (cân nhắc): sau khi user click effect, Volume block xuất hiện ngay dưới → cần đảm bảo subsequent action không bị che. Có thể KHÔNG cần sửa (smartSwipeToFind vẫn dùng UiAutomator resource-id + text).

---

## Utility hiện tại

### GestureUtils.java

- ✅ `swipe(driver, sx, sy, ex, ey, durationMs)` — đủ dùng cho drag volume slider
- ❌ `dragFromTo` / `tapAndHold` — chưa có, nhưng `swipe()` đã làm được — KHÔNG cần add

### RecordFlowHelper.java

- ✅ `navigateToVoiceEffects(driver, recordSeconds)` đã có
- ✅ `smartResetToHome` xử lý Voice Effects close + Discard dialog

---

## Test classes hiện tại — RISK ASSESSMENT

| Class | Test count | Risk | Lý do |
|-------|------------|------|-------|
| `VoiceEffects01_Verify_UI_Display` | 8 (VE_01_01..08) | **HIGH** | Test `VE_01_08` assert `foundCount >= 21` cho 23 effect cũ. List cũ có "Fade" — KHÔNG còn trong DOM mới → có thể fail. Cần update logic hoặc đổi ALL_EFFECTS. |
| `VoiceEffects02_Verify_Audio_Player` | 4 (VE_02_01..04) | **LOW** | Chỉ dùng play/pause/time — locator không đổi |
| `VoiceEffects03_Verify_Apply_Effects` | 25 (VE_03_01..25) | **HIGH** | Test `VE_03_23_fade` click effect "Fade" — KHÔNG còn → SẼ FAIL. Các test khác dùng effect cũ (Normal, Man, Woman, ..., Bee) đều còn → OK. Volume block ĐỘNG có thể nhiễu smart-swipe nhưng dùng resource-id nên OK. |
| `VoiceEffects04_Verify_Save_Close` | 6 | **LOW** | Save/Discard flow không bị ảnh hưởng. `VE_04_02_save_with_robot_effect` dùng "Robot" còn trong list → OK |
| `VoiceEffects05_Verify_Send_Voice_Message` | 3 (VE_06_01..03) | **LOW** | Share sheet flow không bị ảnh hưởng |

### Test SẼ FAIL nếu không xử lý

1. **`VE_03_23_fade`** — effect "Fade" KHÔNG còn trong DOM mới
   - Option A: **Đổi sang effect khác** (vd "Old Radio") + đổi test ID → vi phạm "KHÔNG đổi test ID"
   - Option B: **Skip test** + comment lý do → giữ ID nhưng không run
   - Option C: **Xóa hẳn** test `VE_03_23` → vi phạm "KHÔNG xóa test"
   - **Đề xuất**: Option B (skip với `@Test(enabled = false)` + comment) — tránh false failure, không vi phạm rule

2. **`VE_01_08`** — assert `foundCount >= 21` trong 23 effects
   - Vì list cũ có "Fade" mà DOM không có → tối đa chỉ 22 → vẫn PASS (>=21)
   - **Nhưng** test sẽ log "Missing: Fade" trong warning → cosmetic issue
   - **Đề xuất**: Update `ALL_EFFECTS` constant để bao gồm 34 effects mới (bỏ "Fade"). Test sẽ tự reflect số mới.

### Test cần ADD MỚI

1. **Volume control feature** (file mới `VoiceEffects06_Verify_Volume.java`):
   - VE_07_01: Volume section UI displayed
   - VE_07_02: Default value = "100"
   - VE_07_03: Drag volume → value thay đổi
   - VE_07_04: Click Reset → value về "100"

2. **12 effect mới** (file mới `VoiceEffects07_Verify_New_Effects.java`):
   - VE_08_01..12 cho: Whisper, Cyborg, Devil, Ghost, Tremolo, Bathroom, Cave, Hall, Stadium, Tunnel, Megaphone, Old Radio

3. **Dynamic Volume position** (optional, có thể gộp vào file Volume hoặc skip):
   - VE_07_05: Verify Volume block move khi chọn effect khác
   - **Cân nhắc**: test này khó assert robust (cần check bounds Y của Volume tăng khi chọn effect ở dưới). Đề xuất SKIP nếu chỉ làm phase 3 lần đầu.

---

## Đề xuất changes

### Phase 1: Update Page Object (LOW-MEDIUM RISK)

**File**: `src/main/java/Pages/VoiceEffectsPage.java`

1. **Update `ALL_EFFECTS` list** từ 23 → 34 effects (đã liệt kê ở trên)
2. **THÊM 4 locator Volume** + **~11 method Volume**
3. **KHÔNG** sửa method effect/audio cũ
4. **KHÔNG** sửa locator cũ

**Verify**: `.\gradlew clean compileJava` SUCCESS

### Phase 2: Fix test cũ bị break + verify (MEDIUM RISK)

1. **`VoiceEffects03_Verify_Apply_Effects.java`**:
   - Disable `test_VE_03_23_fade` bằng `@Test(enabled = false, description = "VE_03_23: Fade (DISABLED - effect removed from DOM)")` — giữ ID, không run
2. **`VoiceEffects01_Verify_UI_Display.java`**:
   - Test `VE_01_08` sẽ tự reflect list mới sau khi update `ALL_EFFECTS`. Có thể update message assertion (vd "Found X/34") để rõ ràng hơn — *optional*.
3. **KHÔNG** đổi test ID, KHÔNG đổi logic.
4. Build: `.\gradlew clean compileTestJava` SUCCESS
5. Smoke run: `.\gradlew test --tests "*VoiceEffects01*"` — kỳ vọng 8/8 PASS

### Phase 3: Thêm test mới (LOW RISK)

**File mới**:
- `src/test/java/testcases/Voiceeffects/VoiceEffects06_Verify_Volume.java` — 4 test Volume (VE_07_01..04)
- `src/test/java/testcases/Voiceeffects/VoiceEffects07_Verify_New_Effects.java` — 12 test effect mới (VE_08_01..12)

**Update**: `src/test/resources/Regression.xml` — thêm 2 class mới vào `<test name="Voice Effects Tests">`

**Smoke**:
- `.\gradlew test --tests "*VoiceEffects06*"` → kỳ vọng 4/4 PASS
- `.\gradlew test --tests "*VoiceEffects07*"` → kỳ vọng 12/12 PASS

---

## Estimate time

| Phase | Time |
|-------|------|
| Phase 1: Page Object (update ALL_EFFECTS + thêm Volume methods) | ~15 phút code |
| Phase 2: Disable Fade test + verify build + smoke 1 class | ~5 phút code + ~5-7 phút smoke |
| Phase 3: VolumeTest + NewEffectsTest + xml update + smoke | ~20-25 phút code + ~10 phút smoke |
| **Tổng** | **~55-70 phút** (chưa kể appium boot/recording) |

---

## ⏸ STOP — Câu hỏi cần user confirm

1. **"Fade" effect handling**: chọn cách nào?
   - (A) Disable bằng `@Test(enabled = false)` — giữ ID, đánh dấu rõ DOM removed *(đề xuất)*
   - (B) Xóa hẳn test `VE_03_23` (vi phạm rule "không xóa test")
   - (C) Đổi sang effect khác như "Old Radio" + đổi tên test (vi phạm "không đổi test ID")

2. **Naming convention test ID mới**: dùng prefix nào?
   - VE_07_* cho Volume (4 test), VE_08_* cho New Effects (12 test) — *đề xuất*
   - Hay đặt khác?

3. **Test "Dynamic Volume position"** (VE_07_05 verify Volume move khi chọn effect khác): có muốn làm không?
   - Khó assert robust → đề xuất SKIP lần này, để feature observation thôi
   - Hoặc làm nhẹ: chỉ verify `isVolumeSectionDisplayed()` còn `true` sau khi chọn effect khác (không assert position cụ thể)

4. **API `dragVolumeTo`**: dùng `double percent` (0.0..1.0) hay `int value` (0..200)?
   - `double percent` — đề xuất, dễ tính từ width của seekBar bounds
